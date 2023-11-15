/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.logic.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import xray.evs.dao.ItemAccess;
import xray.evs.dao.PollAccess;
import xray.evs.dao.QuestionAccess;
import xray.evs.dao.TokenAccess;
import xray.evs.dto.Item;
import xray.evs.dto.MultipleChoiceQuestion;
import xray.evs.dto.Organizer;
import xray.evs.dto.Participant;
import xray.evs.dto.Poll;
import xray.evs.dto.Question;
import xray.evs.dto.Token;
import xray.evs.entities.ItemEntity;
import xray.evs.entities.MultipleChoiceQuestionEntity;
import xray.evs.entities.OrganizerEntity;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.PollEntity;
import xray.evs.entities.QuestionEntity;
import xray.evs.entities.TokenEntity;
import xray.evs.logic.ParticipantLogic;
import xray.evs.utilities.LocalDateAttributeConverter;
import xray.evs.utilities.PollState;
import xray.evs.utilities.QuestionType;

/**
 *
 * @author Ahmad
 */
@Stateless
public class ParticipantLogicImpl implements ParticipantLogic {

    @EJB
    private TokenAccess tokenAccess;

    @EJB
    private ItemAccess itemAccess;

    @EJB
    private QuestionAccess questionAccess;

    @EJB
    private PollAccess pollAccess;

    @Override
    public Token getByToken(String token) {
        TokenEntity tokenEntity = tokenAccess.getByToken(token);
        return createDTO(tokenEntity);
    }

    @Override
    public boolean submitPoll(Token token) {
        Poll poll = token.getPoll();
        PollEntity pe = pollAccess.getPollByUuid(poll.getUuid(),null);
        boolean hasParticipantVotes = false;

        System.out.println("Token is: " + token);

        if (pe.getEndDate().isBefore(LocalDateTime.now())) {
            System.out.println("Seems to be an expired poll!");
            pe.setTokens(null);
            pe.setPollState(PollState.FINISHED);
            return false;
        }

        if (pe.getStartDate().isBefore(LocalDateTime.now())) {
            for (Question question : poll.getQuestions()) {
                QuestionEntity qe = questionAccess.getQuestionByUuid(question.getUuid());

                if (question.isAbsent()) {
                    qe.setAbstentions(qe.getAbstentions() + 1);
                } else {
                    int newVotes = 0;

                    for (Item item : question.getItems()) {
                        if (item.isParticipantVote()) {
                            ItemEntity ie = itemAccess.getItemByUuid(item.getUuid());
                            int vote = ie.getVotesCounter() + 1;
                            ie.setVotesCounter(vote);
                            newVotes++;
                        }
                    }
                    if (newVotes > 0) {
                        qe.setVotes(qe.getVotes() + newVotes);
                        hasParticipantVotes = true;
                    }
                }
            }

            if (hasParticipantVotes) {
                pe.setVoteCounter(pe.getVoteCounter() + 1);
            }

            tokenAccess.deleteToken(token.getToken());

            if (pe.getTokens() == null || pe.getTokens().isEmpty()) {
                pe.setPollState(PollState.FINISHED);
            }
            System.out.println("The vote is submitted!");

            return true;
        }
        System.out.println("The vote was not submitted to the poll as Start Date is in  the future!");

        return false;
    }

    @Override
    public Poll getPollByResultToken(String resultToken) {
        PollEntity pe = pollAccess.getPollByResultToken(resultToken);
        return pe == null ? null : createDTO(pe);
    }

    private Organizer createDTO(OrganizerEntity oe) {
        return new Organizer(oe.getUuid(), oe.getJpaVersion(), oe.getEmail(), oe.getFirstName(), oe.getLastName());
    }

    private Poll createDTO(PollEntity pe) {
        return new Poll(pe.getUuid(),
                pe.getJpaVersion(),
                pe.getTitle(),
                LocalDateAttributeConverter.convertToDateViaSqlTimestamp(pe.getStartDate()),
                LocalDateAttributeConverter.convertToDateViaSqlTimestamp(pe.getEndDate()),
                pe.getDescription(),
                pe.getPollState(),
                pe.getResultToken(),
                pe.getVoteCounter(),
                pe.getTrackingIsEnabled(),
                getOrganizerList(pe.getOrganizers()),
                getQuestionList(pe.getQuestions()),
                getParticipants(pe.getParticipants()));
    }

    private Token createDTO(TokenEntity token) {
        Poll poll = createDTO(token.getPoll());
        if (poll.getTrackingIsEnabled()) {
            Participant p = createDTO(token.getParticipant());
            return new Token(token.getUuid(), token.getJpaVersion(), token.getTokenValue(), p, poll);
        } else {
            return new Token(token.getUuid(), token.getJpaVersion(), token.getTokenValue(), null, poll);
        }
    }

    private Question createDTO(QuestionEntity qe) {
        if (qe.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            return new MultipleChoiceQuestion(qe.getUuid(),
                    qe.getJpaVersion(),
                    qe.getTitle(),
                    getItemList(qe.getItems()),
                    qe.getAbstentions(),
                    qe.getVotes(),
                    ((MultipleChoiceQuestionEntity) qe).getMinChoices(),
                    ((MultipleChoiceQuestionEntity) qe).getMaxChoices());
        } else {
            return new Question(
                    qe.getUuid(),
                    qe.getJpaVersion(),
                    qe.getTitle(),
                    qe.getQuestionType(),
                    qe.getVotes(),
                    qe.getAbstentions(),
                    getItemList(qe.getItems()));
        }
    }

    private Participant createDTO(ParticipantEntity pe) {
        return new Participant(pe.getUuid(), pe.getJpaVersion(), pe.getEmail());
    }

    private Item createDTO(ItemEntity ie) {
        return new Item(
                ie.getUuid(),
                ie.getJpaVersion(),
                ie.getShortName(),
                ie.getDescription(),
                ie.getVotesCounter());
    }

    public List<Question> getQuestionList(List<QuestionEntity> questionEntity) {
        return questionEntity.stream().map(this::createDTO).collect(Collectors.toList());
    }

    public List<Organizer> getOrganizerList(List<OrganizerEntity> organizerEntity) {
        return organizerEntity.stream().map(this::createDTO).collect(Collectors.toList());
    }

    public List<Participant> getParticipants(List<ParticipantEntity> participantEntity) {
        return participantEntity.stream().map(this::createDTO).collect(Collectors.toList());
    }

    public List<Item> getItemList(List<ItemEntity> itemEntity) {
        return itemEntity.stream().map(this::createDTO).collect(Collectors.toList());
    }
}
