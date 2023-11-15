/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.logic.impl;

import java.time.LocalDateTime;
import java.util.Date;
import xray.evs.dto.*;
import xray.evs.entities.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import xray.evs.dao.PollAccess;
import xray.evs.dto.Poll;
import xray.evs.logic.AdministratorLogic;
import xray.evs.utilities.*;

/**
 *
 * @author Admin
 */
@Stateless
public class AdministratorLogicImpl implements AdministratorLogic {

    @EJB
    private PollAccess pollAccess;

    @EJB
    private MailHandler mailHandler;

    @Override
    @RolesAllowed(ADMINISTRATOR_ROLE)
    public List<Poll> getPollList() {
        return pollAccess.getPollList().stream()
                .map(this::createDTO)
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed(ADMINISTRATOR_ROLE)
    public boolean deletePoll(String uuid) {
        Poll poll = createDTO(pollAccess.getPollByUuid(uuid,null));

        if (pollAccess.deletePoll(uuid, null)) {
            if (poll.getPollState() != PollState.PREPARED && poll.getPollState() != PollState.FINISHED) {
                mailHandler.sendPollDeleteByAdminMail(poll);
            }
            return true;
        }
        return false;
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
