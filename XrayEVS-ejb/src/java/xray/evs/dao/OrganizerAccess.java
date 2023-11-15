/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import xray.evs.entities.OrganizerEntity;

/**
 *
 * @author Ahmad
 */
@Stateless
@LocalBean
public class OrganizerAccess {

    @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;

    public OrganizerEntity getUser(String email) {
        OrganizerEntity oe;
        try {
            oe = em.createNamedQuery("getOrganizerByEmail", OrganizerEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            oe = new OrganizerEntity(true);
            oe.setEmail(email);
            oe.setFirstName("/todo");
            oe.setLastName("/todo");
            em.persist(oe);
        }
        return oe;
    }

}
