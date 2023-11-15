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
import xray.evs.dto.ListParticipators;
import xray.evs.entities.ListParticipatorsEntity;
import xray.evs.entities.ParticipantListEntity;

/**
 *
 * @author Ahmad
 */
@LocalBean
@Stateless
public class ListParticipatorsAccess {

    @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;

    public ListParticipatorsEntity createParticipantInList(ParticipantListEntity ple, ListParticipators p) {
        ListParticipatorsEntity pe = new ListParticipatorsEntity(true);
        pe.setEmail(p.getEmail());
        pe.setParticipantList(ple);
        em.persist(ple);
        return pe;
    }

    public List<ListParticipatorsEntity> createParticipantInList(ParticipantListEntity participantListEntity, List<ListParticipators> participants) {
        return participants.stream().map(p -> ListParticipatorsAccess.this.createParticipantInList(participantListEntity, p)).collect(Collectors.toList());
    }

}
