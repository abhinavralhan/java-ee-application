/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xray.evs.web;

import xray.evs.dto.Organizer;
import xray.evs.dto.ListParticipators;
import xray.evs.dto.ParticipantList;
import xray.evs.logic.OrganizerLogic;
import evs.web.i18n.Messages;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * 
 * @author Team Xray
 */
@ViewScoped
@Named
public class ParticipantListEditBean implements Serializable {

    private static final long serialVersionUID  = 1L;

    @EJB
    private OrganizerLogic ol;

    private boolean success;
    private boolean failure;
    private String failureMessage;
    private ParticipantList participantList;
    private String uuid;

    private Set<String> emailList;
    private String inputEmail;
    private UIComponent addParticipantComponent;

    public boolean isValid() {
        return uuid != null && getParticipantList() != null;
    }

    public boolean isSuccess() {
        return success && !FacesContext.getCurrentInstance().isValidationFailed();
    }

    public boolean isFailure() {
        return failure && !FacesContext.getCurrentInstance().isValidationFailed();
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public OrganizerLogic getOl() {
        return ol;
    }

    public void setOl(OrganizerLogic ol) {
        this.ol = ol;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public Organizer getOrganizer() {
        return getOl().getCurrentUser();
    }

    public Set<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(Set<String> emailList) {
        this.emailList = emailList;
    }

    public String getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail.toLowerCase();
    }

    public ParticipantList getParticipantList() {
        if (participantList == null) {
            if ("new".equals(uuid)) {
                participantList = new ParticipantList();
            } else {
                participantList = ol.getParticipantList(uuid);
            }
        }
        if (participantList != null) {
            emailList = getEmailsAsStringSet();
        }

        return participantList;
    }

    private Set<String> getEmailsAsStringSet() {
        Set<String> emailsAsStrings = new HashSet<>();

        List<ListParticipators> participants = participantList.getParticipants();
        participants.forEach((participant) -> {
            emailsAsStrings.add(participant.getEmail());
        });

        return emailsAsStrings;
    }

    public void storeParticipantList() {

        try {
            uuid = ol.storeParticipantList(participantList);
            if (uuid != null) {
                success = true;
                failure = false;
                inputEmail = "";
                participantList = null;
            }
        } catch (EJBException e) {
            success = false;
            failure = true;
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }

            failureMessage = t.getMessage();
            if (failureMessage != null && failureMessage.startsWith("Duplicate entry")) {
                failureMessage = Messages.getMessage("participantListDuplicateNameMessage", participantList.getName());
            }
        }
    }

    public void deleteParticipantList() {
        ol.deleteParticipantList(uuid);
    }
    
    public UIComponent getAddParticipantComponent() {
        return addParticipantComponent;
    }

    public void setAddParticipantComponent(UIComponent addParticipantComponent) {
        this.addParticipantComponent = addParticipantComponent;
    }

    public void addNewParticipant() {
        if (!inputEmail.equals("")) {
            if (emailList.add(inputEmail)) {
                ListParticipators participant = new ListParticipators();
                participant.setEmail(inputEmail);
                participantList.addParticipant(participant);
                inputEmail = "";
            } else {
                FacesContext.getCurrentInstance().addMessage(addParticipantComponent.getClientId(), new FacesMessage(Messages.getMessage("duplicateMailMessage", inputEmail)));
                inputEmail = "";
            }
        }
    }

    public void removeParticipant(ListParticipators participant) {
        participantList.removeParticipant(participant);
        emailList.remove(participant.getEmail());
    }

}
