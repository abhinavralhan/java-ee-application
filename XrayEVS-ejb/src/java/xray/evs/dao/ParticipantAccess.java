/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dao;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import xray.evs.dto.Participant;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.PollEntity;

/**
 *
 * @author Ahmad
 */
@LocalBean
@Stateless
public class ParticipantAccess {

    @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;

    public List<ParticipantEntity> createParticipant(List<Participant> participants, List<PollEntity> p) {
        return participants.stream().map(n -> createParticipant(n, p)).collect(Collectors.toList());
    }

    public ParticipantEntity createParticipant(Participant participant, List<PollEntity> p) {
        ParticipantEntity pe = new ParticipantEntity(true);
        pe.setEmail(participant.getEmail());
        pe.setPoll(p);

        em.persist(pe);
        return pe;
    }
}
