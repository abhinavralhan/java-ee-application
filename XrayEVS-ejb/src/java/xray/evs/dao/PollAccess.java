/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dao;

import xray.evs.dto.Participant;
import xray.evs.dto.Question;
import xray.evs.entities.OrganizerEntity;
import xray.evs.entities.PollEntity;
import xray.evs.utilities.PollState;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import xray.evs.dto.Organizer;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.QuestionEntity;

/**
 *
 * @author Ahmad
 */
@LocalBean
@Stateless
public class PollAccess {

    @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;

    @EJB
    private QuestionAccess questionAccess;

    @EJB
    private ParticipantAccess participantAccess;

    public PollEntity createPoll(
            List<OrganizerEntity> organizer,
            String title,
            LocalDateTime startdate,
            LocalDateTime endDate,
            String description,
            String resultToken,
            Integer voteCount,
            Boolean trackingIsEnabled,
            List<Participant> participants,
            List<Question> questions,
            PollState pollState) {

        PollEntity p = new PollEntity(true);
        p.setDescription(description);
        p.setTitle(title);
        p.setStartDate(startdate);
        p.setEndDate(endDate);
        p.setPollState(pollState);
        p.setResultToken(resultToken);
        p.setVoteCounter(voteCount);
        p.setTrackingIsEnabled(trackingIsEnabled);
        p.setOrganizers(organizer);

        for (OrganizerEntity o : organizer) {
            o.getPolls().add(p);
        }

        em.persist(p);

        List<PollEntity> pl = new ArrayList<PollEntity>();
        pl.add(p);

        p.setParticipants(participantAccess.createParticipant(participants, pl));
        p.setQuestions(questionAccess.createQuestion(questions, p));

        return p;
    }

    public List<PollEntity> getPollList() {
        return em.createNamedQuery("getAllPolls").getResultList();
    }

    public List<PollEntity> getPollList(OrganizerEntity organizers) {
        return em.createNamedQuery("getPollList")
                .setParameter("organizers", organizers)
                .getResultList();
    }

    public PollEntity getPollByUuid(String uuid, OrganizerEntity organizer) {
        if (organizer == null) {
            return em.createNamedQuery("getPollByUuidAdmin", PollEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } else {
            return em.createNamedQuery("getPollByUuid", PollEntity.class)
                    .setParameter("uuid", uuid)
                    .setParameter("organizer", organizer)
                    .getSingleResult();
        }
    }

    public boolean deletePoll(String uuid, OrganizerEntity organizer) {
        PollEntity p = getPollByUuid(uuid, organizer);
        if (p == null) {
            return false;
        }
        p.setOrganizers(null);
        p.setParticipants(null);
        p.setQuestions(null);
        em.remove(p);
        return true;
    }

    public List<String> getListOfPollTitle() {
        return em.createNamedQuery("getListOfPollTitle")
                .getResultList();
    }

    public PollEntity getPollByResultToken(String token) {
        try {
            PollEntity ce = em.createNamedQuery("getPollByResultToken", PollEntity.class)
                    .setParameter("resulttoken", token)
                    .getSingleResult();
            return ce;
        } catch (NoResultException e) {
            return null;
        }
    }
}
