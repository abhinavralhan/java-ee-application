/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dao;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import xray.evs.entities.OrganizerEntity;
import xray.evs.entities.ParticipantListEntity;

/**
 *
 * @author Ahmad
 */

@Stateless
@LocalBean
public class ParticipantListAccess {
   @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;
   
   
    public ParticipantListEntity createParticipantList(OrganizerEntity organizer, String Name) {
        ParticipantListEntity listEntity = new ParticipantListEntity(true);
        listEntity.setName(Name);
        listEntity.setOrganizer(organizer);
        em.persist(listEntity);
        return listEntity;
    }
    
    public List<ParticipantListEntity> getParticipantLists(OrganizerEntity organizer) {
        return em.createNamedQuery("getParticipantLists")
                .setParameter("organizer", organizer)
                .getResultList();
    }
    
     public ParticipantListEntity getParticipantListByUuid(OrganizerEntity organizer, String uuid) {
        try {
            ParticipantListEntity ce = em.createNamedQuery("getParticipantListByUuid", ParticipantListEntity.class)
                    .setParameter("organizer", organizer)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
            return ce;
        } catch (NoResultException e) {
            return null;
        }
    }
     
      public boolean deleteParticipantList(OrganizerEntity organizer, String uuid) {
        ParticipantListEntity ce = getParticipantListByUuid(organizer, uuid);
        if (ce == null) {
            return false;
        }
        ce.getOrganizer().getParticipentLists().remove(ce);
        ce.setOrganizer(null);
        em.remove(ce);
        return true;
    }
     
     
     


    
}
