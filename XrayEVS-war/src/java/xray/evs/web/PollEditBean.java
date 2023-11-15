/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xray.evs.web;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import xray.evs.dto.Item;
import xray.evs.dto.MultipleChoiceQuestion;
import xray.evs.dto.Participant;
import xray.evs.dto.ParticipantList;
import xray.evs.dto.Poll;
import xray.evs.dto.Question;
import xray.evs.utilities.PollState;
import xray.evs.utilities.QuestionType;
import xray.evs.logic.OrganizerLogic;
import evs.web.i18n.Messages;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;

/**
 * 
 * @author Team Xray
 */
@ViewScoped
@Named
public class PollEditBean implements Serializable {

    private static final long serialVersionUID  = 1L;

    @EJB
    private OrganizerLogic ol;

    private boolean success;
    private String successMessage;
    private boolean failure;
    private String failureMessage;
    private Poll poll;
    private String oldTitle;
    private List<String> pollTitles;

    private String uuid;

    private Set<String> emailList;
    private String tempMail;
    private UIComponent addParticipantComponent;
    private List<ParticipantList> participantLists;
    private Map<ParticipantList, Boolean> participantListsCheckMap;

    private Question newQuestion;
    private boolean questionAddMode = false;
    private UIComponent storeQuestionButton;
    private Set<String> questionList;
    private String tempItemName = "";
    private String tempItemDescription;
    private UIComponent addItemComponent;

    private UIComponent titleComponent;

    public boolean isValid() {
        return uuid != null && getPoll() != null;
    }

    public boolean isSuccess() {
        return success && !FacesContext.getCurrentInstance().isValidationFailed();
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public boolean isFailure() {
        return failure && !FacesContext.getCurrentInstance().isValidationFailed();
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Poll getPoll() {
        if (poll == null) {
            if ("new".equals(uuid)) {
                poll = new Poll();
            } else {
                poll = ol.getPoll(uuid);
            }
        }
        if (poll != null) {
            emailList = getEmailsAsStringSet();
            
            if (oldTitle == null) {
                oldTitle = poll.getTitle();
            }
            questionList = getQuestionsAsStringSet();

            participantLists = ol.getParticipantLists();
            participantListsCheckMap = new HashMap<>();

            participantLists.forEach(list -> {
                participantListsCheckMap.put(list, Boolean.FALSE);
            });
        }

        return poll;
    }

    public void storePoll() {
        try {
            if (poll.getEndDate().after(poll.getStartDate()) && poll.getEndDate().after(new Date())) {
                if (isTitleAvailable()) {
                    uuid = ol.storePoll(poll);
                    if (uuid != null) {
                        success = true;
                        successMessage = Messages.getMessage("editSuccessMessage");
                        failure = false;
                        // force reload since the JPA version might have changed
                        oldTitle = poll.getTitle();
                        poll = null;
                    }
                } else {
                    success = false;
                    failure = true;
                    failureMessage = Messages.getMessage("titleNotAvailableMessage");

                }

            } else {
                success = false;
                failure = true;
                failureMessage = Messages.getMessage("pollEditEndDateBeforeStartDateMessage");

            }
        } catch (EJBException e) {
            success = false;
            failure = true;
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            failureMessage = t.getMessage();
        }
    }

    public boolean isPollPrepared() {
        if (poll != null) {
            if (poll.getPollState() != null) {
                return poll.getPollState().equals(PollState.PREPARED);
            }
        }
        return false;
    }

    public boolean isPollStarted() {
        if (poll != null) {
            if (poll.getPollState() != null) {
                return poll.getPollState().equals(PollState.STARTED);
            }
        }
        return false;
    }

    public boolean isPollVoting() {
        if (poll != null) {
            if (poll.getPollState() != null) {
                return poll.getPollState().equals(PollState.VOTING);
            }
        }
        return false;
    }

    public boolean isPollFinished() {
        if (poll != null) {
            if (poll.getPollState() != null) {
                return poll.getPollState().equals(PollState.FINISHED);
            }
        }
        return false;
    }

    public void startPoll() {
        if (isPollValid()) {
            try {
                uuid = ol.startPoll(poll);
                if (uuid != null) {
                    success = true;
                    successMessage = Messages.getMessage("startPollSuccessMessage");
                    failure = false;
                    // force reload since the JPA version might have changed
                    poll = null;
                }
            } catch (EJBException e) {
                success = false;
                failure = true;
                Throwable t = e;
                while (t.getCause() != null) {
                    t = t.getCause();
                }
                failureMessage = t.getMessage();
            }
        } else {
            success = false;
            failure = true;
        }
    }

    public boolean isParticpantListVaild(Set<String> list) {
        if (list != null && !list.isEmpty() && list.size() > 2) {
            return true;
        }
        return false;
    }

    public boolean isQuestionValid(List<Item> items) {
        if (items != null && !items.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean isQuestionListValid(List<Question> questions) {
        if (questions != null && !questions.isEmpty()) {
            // return !questions.stream().anyMatch((question) -> (!isQuestionValid(question.getItems()) ));
            return true;
        }
        return false;
    }

    public boolean isPollValid() {
        if (!isTitleAvailable()) {
            failureMessage = Messages.getMessage("titleNotAvailableMessage");
            return false;
        }
        if (!(poll.getEndDate().after(poll.getStartDate()) && poll.getEndDate().after(new Date()))) {
            failureMessage = Messages.getMessage("pollEditEndDateBeforeStartDateMessage");
            return false;

        }
        if (isParticpantListVaild(emailList)) {
            if (isQuestionListValid(poll.getQuestions())) {
                return true;
            } else {
                failureMessage = Messages.getMessage("questionListInvalid");
                return false;
            }

        } else {
            failureMessage = Messages.getMessage("participantListInvalidMessage");
            return false;
        }
    }

    public void deletePoll() {
        ol.deletePoll(uuid);
    }

    private Set<String> getEmailsAsStringSet() {
        Set<String> emailsAsStrings = new HashSet<>();
        List<Participant> participants = poll.getParticipants();
        participants.forEach((participant) -> {
            emailsAsStrings.add(participant.getEmail());
        });

        return emailsAsStrings;
    }

    public String getTempMail() {
        return tempMail;
    }

    public void setTempMail(String tempMail) {
        this.tempMail = tempMail.toLowerCase();
    }

    public UIComponent getAddParticipantComponent() {
        return addParticipantComponent;
    }

    public void setAddParticipantComponent(UIComponent addParticipantComponent) {
        this.addParticipantComponent = addParticipantComponent;
    }

    public List<ParticipantList> getParticipantLists() {
        return participantLists;
    }

    public void setParticipantLists(List<ParticipantList> participantLists) {
        this.participantLists = participantLists;
    }

    public Map<ParticipantList, Boolean> getParticipantListsCheckMap() {
        return participantListsCheckMap;
    }

    public List<ParticipantList> getSelectedParticipantLists() {
        List<ParticipantList> result = new ArrayList<>();
        for (Entry<ParticipantList, Boolean> entry : participantListsCheckMap.entrySet()) {
            if (entry.getValue()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public void importParticipantLists() {

        List<ParticipantList> selectedLists = getSelectedParticipantLists();

        selectedLists.forEach((ParticipantList list) -> {
            list.getParticipants().forEach(participantInList -> {

                if (emailList.add(participantInList.getEmail())) {
                    Participant newParticipant = new Participant();
                    newParticipant.setEmail(participantInList.getEmail());
                    poll.addParticipant(newParticipant);
                }
            });
        });
    }

    public void addNewParticipant() {
        if (!tempMail.equals("")) {
            if (emailList.add(tempMail)) {
                Participant participant = new Participant();
                participant.setEmail(tempMail);

                poll.addParticipant(participant);

                tempMail = "";
            } else {
                FacesContext.getCurrentInstance().addMessage(addParticipantComponent.getClientId(), new FacesMessage(Messages.getMessage("duplicateMailMessage", tempMail)));
                tempMail = "";
            }
        }
    }

    public void removeParticipant(Participant participant) {
        poll.removeParticipant(participant);
        emailList.remove(participant.getEmail());
    }

    public boolean isQuestionAddMode() {
        return questionAddMode;
    }

    public void setQuestionAddMode(boolean questionAddMode) {
        this.questionAddMode = questionAddMode;
    }

    public Question getNewQuestion() {
        return newQuestion;
    }

    public void setNewQuestion(Question newQuestion) {
        this.newQuestion = newQuestion;
    }

    public UIComponent getStoreQuestionButton() {
        return storeQuestionButton;
    }

    public void setStoreQuestionButton(UIComponent storeQuestionButton) {
        this.storeQuestionButton = storeQuestionButton;
    }

    public String getTempItemName() {
        return tempItemName;
    }

    public void setTempItemName(String tempItemName) {
        this.tempItemName = tempItemName;
    }

    public String getTempItemDescription() {
        return tempItemDescription;
    }

    public void setTempItemDescription(String tempItemDescription) {
        this.tempItemDescription = tempItemDescription;
    }

    public UIComponent getAddItemComponent() {
        return addItemComponent;
    }

    public void setAddItemComponent(UIComponent addItemComponent) {
        this.addItemComponent = addItemComponent;
    }

    private Set<String> getQuestionsAsStringSet() {
        Set<String> questionsAsString = new HashSet<>();

        List<Question> questions = poll.getQuestions();
        questions.forEach((Question question) -> {
            questionsAsString.add(question.getTitle());
        });

        return questionsAsString;
    }

    public void addNewQuestion(QuestionType questionType) {
        if (questionType == QuestionType.MULTIPLE_CHOICE) {
            newQuestion = new MultipleChoiceQuestion();
            ((MultipleChoiceQuestion) newQuestion).setMinChoices(1);
            ((MultipleChoiceQuestion) newQuestion).setMaxChoices(2);
        } else {
            newQuestion = new Question();
        }
        newQuestion.setQuestionType(questionType);

        if (questionType == QuestionType.YES_NO) {
            Item yesItem = new Item();
            yesItem.setShortName("Yes");
            Item noItem = new Item();
            noItem.setShortName("No");

            newQuestion.addItem(yesItem);
            newQuestion.addItem(noItem);
        }

        questionAddMode = true;
    }

    public void addNewItem() {

        if (!tempItemName.equals("")) {

            Set<String> itemListAsString = new HashSet<>();
            List<Item> items = newQuestion.getItems();
            items.forEach((item) -> {
                itemListAsString.add(item.getShortName());
            });

            if (itemListAsString.add(tempItemName)) {
                Item newItem = new Item();
                newItem.setShortName(tempItemName);

                if (!tempItemDescription.equals("") && tempItemDescription != null) {
                    newItem.setDescription(tempItemDescription);
                }

                newQuestion.addItem(newItem);
            } else {
                FacesContext.getCurrentInstance().addMessage(addItemComponent.getClientId(), new FacesMessage(Messages.getMessage("duplicateItemMessage", tempItemName)));
            }

            tempItemName = "";
            tempItemDescription = "";

        }
    }

    public void storeNewQuestion() {

        if (!newQuestion.getTitle().equals("")) {
            if (questionList.add(newQuestion.getTitle())) {
                if (newQuestion.getItems().size() > 1 || newQuestion.getQuestionType() == QuestionType.YES_NO) {

                    if (newQuestion.getQuestionType() == QuestionType.MULTIPLE_CHOICE && ((MultipleChoiceQuestion) newQuestion).getMinChoices() > ((MultipleChoiceQuestion) newQuestion).getMaxChoices()) {
                        FacesContext.getCurrentInstance().addMessage(storeQuestionButton.getClientId(), new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.getMessage("questionEditMinGreaterThanMaxMessage"), null));
                    } else if (newQuestion.getQuestionType() == QuestionType.MULTIPLE_CHOICE && ((MultipleChoiceQuestion) newQuestion).getMinChoices() > newQuestion.getItems().size()) {
                        FacesContext.getCurrentInstance().addMessage(storeQuestionButton.getClientId(), new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.getMessage("questionEditNotEnoughItemsMessage"), null));
                    } else {
                        poll.addQuestion(newQuestion);
                        questionAddMode = false;
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(storeQuestionButton.getClientId(), new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.getMessage("questionEditNoResponeOptionsMessage"), null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(storeQuestionButton.getClientId(), new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.getMessage("questionDuplicateTitleMessage", newQuestion.getTitle()), null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(storeQuestionButton.getClientId(), new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.getMessage("questionEditNoTitleMessage"), null));
        }

    }

    public void removeQuestion(Question question) {
        poll.removeQuestion(question);
        questionList.remove(question.getTitle());
    }

    public boolean hasEnoughVotes() {
        return poll.getVoteCounter() >= 3;
    }

    public boolean isTitleAvailable() {
        if (!poll.isNew()) {
            if (oldTitle.equals(poll.getTitle())) {
                return true;
            }
        }
        if (!ol.getListOfPollTitle().contains(poll.getTitle())) {
            FacesContext.getCurrentInstance().addMessage(titleComponent.getClientId(), new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.getMessage("titleAvailableMessage"), null));
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(titleComponent.getClientId(), new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.getMessage("titleNotAvailableMessage"), null));
            return false;
        }
    }

    public void sendReminders() {
        if (poll.getTrackingIsEnabled()) {
            try {
                uuid = ol.sendReminders(poll.getUuid());
                if (uuid != null) {
                    success = true;
                    successMessage = Messages.getMessage("sendReminderSuccessMessage");
                    failure = false;
                    // force reload since the JPA version might have changed
                    poll = null;
                }
            } catch (EJBException e) {
                success = false;
                failure = true;
                Throwable t = e;
                while (t.getCause() != null) {
                    t = t.getCause();
                }
                failureMessage = t.getMessage();
            }
        }
    }
    
    public UIComponent getTitleComponent() {
        return titleComponent;
    }

    public void setTitleComponent(UIComponent titleComponent) {
        this.titleComponent = titleComponent;
    }
}
