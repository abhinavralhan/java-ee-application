
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xray.evs.web;

import xray.evs.dto.ListParticipators;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import xray.evs.dto.ParticipantList;
import xray.evs.logic.OrganizerLogic;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Team Xray
 */
@RequestScoped
@Named
public class ParticipantListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrganizerLogic ol;

    private List<ParticipantList> participantLists;

    public List<ParticipantList> getParticipantLists() {
        if (participantLists == null) {
            participantLists = ol.getParticipantLists();
        }
        return participantLists;
    }

    public void deleteParticipantList(ParticipantList participantList) {
        ol.deleteParticipantList(participantList.getUuid());
        participantLists = ol.getParticipantLists();
    }
}
