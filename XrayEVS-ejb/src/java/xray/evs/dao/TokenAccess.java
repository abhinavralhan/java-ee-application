/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dao;

import java.util.ArrayList;
import java.util.List;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.PollEntity;
import xray.evs.entities.TokenEntity;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import xray.evs.utilities.Tokens;

/**
 *
 * @author Ahmad
 */
@LocalBean
@Stateless
public class TokenAccess {

    @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;

    public TokenEntity createToken(String tokenValue, ParticipantEntity participantEntity, PollEntity pollEntity) {
        TokenEntity te = new TokenEntity(true);
        te.setPoll(pollEntity);
        te.setTokenValue(tokenValue);
        te.setParticipant(participantEntity);
        em.persist(te);
        return te;
    }

    public TokenEntity createToken(PollEntity pollEntity ,ParticipantEntity participantEntity, Boolean trackingEnabled) {
        String token = Tokens.generateToken();
        TokenEntity te = new TokenEntity(true);
        te.setTokenValue(token);
        if (trackingEnabled) {
            te.setParticipant(participantEntity);
        }
        te.setPoll(pollEntity);
        em.persist(te);
        return te;
    }

    public TokenEntity getByToken(String tokenValue) {
        try {
            TokenEntity te = em.createNamedQuery("getTokenValue", TokenEntity.class)
                    .setParameter("tokenValue", tokenValue)
                    .getSingleResult();
            return te;
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean deleteToken(String tokenValue) {
        TokenEntity te = getByToken(tokenValue);
        em.remove(te);
        return true;
    }
}
