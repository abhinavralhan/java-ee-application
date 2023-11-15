/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.logic.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import xray.evs.dao.ListParticipatorsAccess;
import xray.evs.dao.OrganizerAccess;
import xray.evs.dao.ParticipantAccess;
import xray.evs.dao.ParticipantListAccess;
import xray.evs.dao.PollAccess;
import xray.evs.dao.QuestionAccess;
import xray.evs.dao.TokenAccess;
import xray.evs.dto.Item;
import xray.evs.dto.ListParticipators;
import xray.evs.dto.MultipleChoiceQuestion;
import xray.evs.dto.Organizer;
import xray.evs.dto.Participant;
import xray.evs.dto.ParticipantList;
import xray.evs.dto.Poll;
import xray.evs.dto.Question;
import xray.evs.entities.ItemEntity;
import xray.evs.entities.ListParticipatorsEntity;
import xray.evs.entities.MultipleChoiceQuestionEntity;
import xray.evs.entities.OrganizerEntity;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.ParticipantListEntity;
import xray.evs.entities.PollEntity;
import xray.evs.entities.QuestionEntity;
import xray.evs.entities.TokenEntity;
import xray.evs.logic.OrganizerLogic;
import xray.evs.logic.ParticipantLogic;
import xray.evs.utilities.LocalDateAttributeConverter;
import xray.evs.utilities.MailHandler;
import xray.evs.utilities.PollState;
import xray.evs.utilities.QuestionType;
import xray.evs.utilities.Tokens;

/**
 *
 * @author Admin
 */
@Stateless
public class OrganizerLogicImpl implements OrganizerLogic {

    @EJB
    private OrganizerAccess organizerAccess;

    @EJB
    private ListParticipatorsAccess listParticipatorsAccess;

    @EJB
    private PollAccess pollAccess;

    @EJB
    private ParticipantAccess participantAccess;

    @EJB
    private QuestionAccess questionAccess;

    @EJB
    private TokenAccess tokenAccess;

    @EJB
    private ParticipantListAccess participantListAccess;

    @EJB
    private MailHandler mailHandler;

    private List<OrganizerEntity> organizers;

    @Resource
    private EJBContext ejbContext;

    @AroundInvoke
    private Object getCaller(InvocationContext ctx) throws Exception {
        Principal p = ejbContext.getCallerPrincipal();
        if (p != null) {
            if (organizers == null) {
                organizers = new ArrayList<OrganizerEntity>();
            }
            var x = organizerAccess.getUser(p.getName());
            if (organizers.isEmpty()) {
                organizers.add(x);
            } else {
                organizers.set(0, x);
            }
        }
        return ctx.proceed();
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public Organizer getCurrentUser() {
        return createDTO(organizers.get(0));
    }

    private Organizer createDTO(OrganizerEntity oe) {
        return new Organizer(oe.getUuid(), oe.getJpaVersion(), oe.getEmail(), oe.getFirstName(), oe.getLastName());
    }

    private Item createDTO(ItemEntity ie) {
        return new Item(ie.getUuid(), ie.getJpaVersion(), ie.getShortName(), ie.getDescription(), ie.getVotesCounter());
    }

    private List<Organizer> createDTO(List<OrganizerEntity> oe) {
        List o = new ArrayList<Organizer>();
        for (OrganizerEntity item : oe) {
            o.add(new Organizer(item.getUuid(), item.getJpaVersion(), item.getEmail(), item.getFirstName(), item.getLastName()));
        }
        return o;
    }

    private Poll createDTO(PollEntity pe) {
        return new Poll(pe.getUuid(), pe.getJpaVersion(), pe.getTitle(), LocalDateAttributeConverter.convertToDateViaSqlTimestamp(pe.getStartDate()), LocalDateAttributeConverter.convertToDateViaSqlTimestamp(pe.getEndDate()), pe.getDescription(), pe.getPollState(), pe.getResultToken(), pe.getVoteCounter(), pe.getTrackingIsEnabled(), createDTO(pe.getOrganizers()), getQuestionList(pe.getQuestions()), getParticipants(pe.getParticipants()));
    }

    private ListParticipators createDTO(ListParticipatorsEntity p) {
        return new ListParticipators(p.getUuid(), p.getJpaVersion(), p.getEmail());
    }

    private Participant createDTO(ParticipantEntity pe) {
        return new Participant(pe.getUuid(), pe.getJpaVersion(), pe.getEmail());
    }

    private ParticipantList createDTO(ParticipantListEntity ple) {
        List<ListParticipators> p = ple.getParticipants().stream().map(this::createDTO).collect(Collectors.toList());
        return new ParticipantList(ple.getUuid(), ple.getJpaVersion(), ple.getName(), p);
    }

    private Question createDTO(QuestionEntity qe) {
        if (qe.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            return new MultipleChoiceQuestion(qe.getUuid(), qe.getJpaVersion(), qe.getTitle(), getItemList(qe.getItems()), qe.getAbstentions(), qe.getVotes(), ((MultipleChoiceQuestionEntity) qe).getMinChoices(), ((MultipleChoiceQuestionEntity) qe).getMaxChoices());
        } else {
            return new Question(qe.getUuid(), qe.getJpaVersion(), qe.getTitle(), qe.getQuestionType(), qe.getVotes(), qe.getAbstentions(), getItemList(qe.getItems()));
        }
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public Poll createPoll(String title, LocalDateTime startDate, LocalDateTime endDate, String description, Boolean trackingEnabled, List<Participant> participants, List<Question> questions, PollState pollState) {
        return createDTO(pollAccess.createPoll(organizers, title, startDate, endDate, description, null, 0, trackingEnabled, participants, questions, pollState));
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public String storePoll(Poll poll) {
        if (poll.isNew()) {
            return pollAccess.createPoll(organizers, poll.getTitle(), LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getStartDate()), LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getEndDate()), poll.getDescription(), poll.getResultToken(), poll.getVoteCounter(), poll.getTrackingIsEnabled(), poll.getParticipants(), poll.getQuestions(), PollState.PREPARED).getUuid();
        }
        PollEntity pe = pollAccess.getPollByUuid(poll.getUuid(),organizers.get(0));
        if (pe == null) {
            return null;
        }

        if (pe.getPollState().equals(PollState.PREPARED)) {
            pe.setJpaVersion(poll.getJpaVersion());
            pe.setTitle(poll.getTitle());
            pe.setDescription(poll.getDescription());
            pe.setEndDate(LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getEndDate()));
            pe.setStartDate(LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getStartDate()));
            pe.setTrackingIsEnabled(poll.getTrackingIsEnabled());
            List<PollEntity> pel = new ArrayList<PollEntity>();
            pel.add(pe);
            pe.setParticipants(participantAccess.createParticipant(poll.getParticipants(), pel));
            pe.setQuestions(questionAccess.createQuestion(poll.getQuestions(), pe));
            for (OrganizerEntity o : organizers) {
                o.getPolls().add(pe);
            }
            pe.setOrganizers(organizers);
        } else {
            pe.setEndDate(LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getEndDate()));
            pe.setStartDate(LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getStartDate()));
        }

        return pe.getUuid();
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public boolean deletePoll(String uuid) {
        Boolean sucessful = false;
        Poll poll = createDTO(pollAccess.getPollByUuid(uuid,organizers.get(0)));
        for (OrganizerEntity o : organizers) {
            if (pollAccess.deletePoll(uuid,organizers.get(0))) {
                if (poll.getPollState() != PollState.PREPARED && poll.getPollState() != PollState.FINISHED) {
                    mailHandler.sendPollDeleteMail(poll);
                }
                sucessful = true;
            }
        }
        return sucessful;
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public Poll getPoll(String uuid) {
        PollEntity pe = pollAccess.getPollByUuid(uuid,organizers.get(0));

        return pe == null ? null : createDTO(pe);
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public List<Poll> getPollList() {
        return pollAccess.getPollList(organizers.get(0))
                .stream()
                .map(this::createDTO)
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public List<String> getListOfPollTitle() {
        return pollAccess.getListOfPollTitle();

    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public List<Question> getQuestionList(List<QuestionEntity> questionEntity) {
        return questionEntity.stream().map(this::createDTO).collect(Collectors.toList());
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public List<Participant> getParticipants(List<ParticipantEntity> participantEntity) {
        return participantEntity.stream().map(this::createDTO).collect(Collectors.toList());
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public List<Item> getItemList(List<ItemEntity> itemEntity) {
        return itemEntity.stream().map(this::createDTO).collect(Collectors.toList());
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public String startPoll(Poll poll) {

        PollEntity pe = pollAccess.getPollByUuid(poll.getUuid(),organizers.get(0));

        if (pe == null) {
            return null;
        }

        pe.setJpaVersion(poll.getJpaVersion());
        pe.setTitle(poll.getTitle());
        pe.setStartDate(LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getStartDate()));
        pe.setEndDate(LocalDateAttributeConverter.convertToLocalDateTimeViaSqlTimestamp(poll.getEndDate()));
        pe.setDescription(poll.getDescription());
        pe.setTrackingIsEnabled(poll.getTrackingIsEnabled());

        List<PollEntity> pl = new ArrayList<PollEntity>();
        pl.add(pe);

        pe.setParticipants(participantAccess.createParticipant(poll.getParticipants(), pl));
        pe.setQuestions(questionAccess.createQuestion(poll.getQuestions(), pe));

        for (OrganizerEntity o : organizers) {
            o.getPolls().add(pe);
        }
        pe.setOrganizers(organizers);

        if (pe.getStartDate().isBefore(LocalDateTime.now())) {
            pe.setPollState(PollState.VOTING);
        } else {
            pe.setPollState(PollState.STARTED);
        }

        createToken(pe);

        mailHandler.sendTokenMail(pe);

        return pe.getUuid();
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public String publishPoll(String uuid) {
        PollEntity pe = pollAccess.getPollByUuid(uuid,organizers.get(0));

        if (pe.getVoteCounter() >= 3 && pe.getPollState() == PollState.FINISHED) {
            String resultToken = Tokens.generateToken();
            pe.setResultToken(resultToken);

            Poll poll = createDTO(pe);

            mailHandler.sendResultsMail(poll);
        }

        return pe.getUuid();
    }

    @Override
    public String sendReminders(String uuid) {

        PollEntity pe = pollAccess.getPollByUuid(uuid,organizers.get(0));

        if (pe.getTrackingIsEnabled()) {
            List<TokenEntity> tokens = pe.getTokens();

            mailHandler.sendReminder(tokens, pe);

        }

        return pe.getUuid();
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public void createToken(PollEntity pollEntity) {
        List<ParticipantEntity> entity = pollEntity.getParticipants();
        Boolean trackingIsEnabled = pollEntity.getTrackingIsEnabled();
        for (ParticipantEntity participantEntity : entity) {
            try {
                pollEntity.getTokens().add(tokenAccess.createToken(pollEntity, participantEntity, trackingIsEnabled));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public ParticipantList createParticipantList(String Name, List<ListParticipators> participants) {
        ParticipantListEntity participantListEntity = participantListAccess.createParticipantList(organizers.get(0), Name);
        List<ListParticipatorsEntity> p = listParticipatorsAccess.createParticipantInList(participantListEntity, participants);
        participantListEntity.setParticipants(p);
        return createDTO(participantListEntity);
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public ParticipantList getParticipantList(String uuid) {
        ParticipantListEntity ple = participantListAccess.getParticipantListByUuid(organizers.get(0), uuid);
        return ple == null ? null : createDTO(ple);
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public List<ParticipantList> getParticipantLists() {
        return participantListAccess.getParticipantLists(organizers.get(0))
                .stream()
                .map(this::createDTO)
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public String storeParticipantList(ParticipantList participantList) {
        if (participantList.isNew()) {
            ParticipantListEntity pe = participantListAccess.createParticipantList(organizers.get(0), participantList.getName());
            List<ListParticipatorsEntity> p = listParticipatorsAccess.createParticipantInList(pe, participantList.getParticipants());
            pe.setParticipants(p);
            return pe.getUuid();
        }
        ParticipantListEntity pe = participantListAccess.getParticipantListByUuid(organizers.get(0), participantList.getUuid());
        if (pe == null) {
            return null;
        }
        List<ListParticipatorsEntity> p = listParticipatorsAccess.createParticipantInList(pe, participantList.getParticipants());

        pe.setJpaVersion(participantList.getJpaVersion());
        pe.setName(participantList.getName());
        pe.setParticipants(p);
        return pe.getUuid();
    }

    @Override
    @RolesAllowed(ORGANIZER_ROLE)
    public boolean deleteParticipantList(String uuid) {
        return participantListAccess.deleteParticipantList(organizers.get(0), uuid);
    }
}
