/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xray.evs.web;

import xray.evs.dto.Item;
import xray.evs.dto.MultipleChoiceQuestion;
import xray.evs.dto.Poll;
import xray.evs.dto.Question;
import xray.evs.dto.Token;
import xray.evs.utilities.PollState;
import xray.evs.utilities.QuestionType;
import xray.evs.logic.ParticipantLogic;
import evs.web.i18n.Messages;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * 
 * @author Team Xray
 */
@ViewScoped
@Named
public class VotingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ParticipantLogic pl;

    private String token;
    private Poll poll;
    private Token tokenPoll;

    private boolean success;
    private boolean failure;
    private String failureMessage;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (token != null) {
            this.token = token;
            getPollByToken();
        }
    }

    public boolean isTokenSet() {
        return token != null;
    }

    public void getPollByToken() {
        try {
            if (token != null) {
                tokenPoll = pl.getByToken(token);
                if (tokenPoll.getPoll().getPollState().equals(PollState.VOTING)) {
                    failure = false;
                    poll = tokenPoll.getPoll();
                } else {
                    token = null;
                    failure = true;
                    if (tokenPoll.getPoll().getPollState().equals(PollState.FINISHED)) {
                        failureMessage = Messages.getMessage("votingPollAlreadyEndedMessage");
                    } else {
                        failureMessage = Messages.getMessage("votingPollNotStartedMessage");
                    }
                }
            }
        } catch (Exception e) {
            failure = true;
            failureMessage = Messages.getMessage("votingTokenInvalidMessage");
            token = null;
        }

    }

    public Poll getPoll() {
        return poll;
    }
    
    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public boolean isSuccess() {
        return success && !FacesContext.getCurrentInstance().isValidationFailed();
    }

    public boolean isFailure() {
        return failure && !FacesContext.getCurrentInstance().isValidationFailed();
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void checkChoices(Question question, Item item) {
        MultipleChoiceQuestion choiceQuestion = (MultipleChoiceQuestion) question;
        choiceQuestion.getMaxChoices();
    }

    public boolean isVotesValid() {

        failureMessage = "";
        boolean isValid = true;

        List<Question> questions = poll.getQuestions();
        for (Question question : questions) {
            if (question.getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
                int counter = 0;
                for (Item item : question.getItems()) {
                    if (item.isParticipantVote()) {
                        counter++;
                    }
                }
                if (!(counter >= mcq.getMinChoices() && counter <= mcq.getMaxChoices()) && !question.isAbsent()) {
                    failure = true;
                    failureMessage = failureMessage + Messages.getMessage("votingInvalidAmountAnswersMessage", mcq.getTitle()) + " \n";
                    isValid = false;
                }
            }
            if (question.getQuestionType().equals(QuestionType.SINGLE_CHOICE) | question.getQuestionType().equals(QuestionType.YES_NO)) {
                int counter = 0;
                for (Item item : question.getItems()) {
                    if (item.isParticipantVote()) {
                        counter++;
                    }
                }
                if (counter != 1 && !question.isAbsent()) {
                    failure = true;
                    failureMessage = failureMessage + Messages.getMessage("votingNoVotesForQuestionMessage", question.getTitle()) + " \n";

                    isValid = false;
                }
            }

        }
        return isValid;
    }

    public void submitVote() {
        if (isVotesValid()) {
            pl.submitPoll(tokenPoll);
            token = null;
            success = true;
            failure = false;
        }

    }
}
