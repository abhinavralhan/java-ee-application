/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Danoosh
 */
@Entity
public class ListParticipatorsEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 8169202820084673049L;

    private String email;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public ListParticipatorsEntity(boolean newEntity) {
        super(newEntity);
    }

    public ListParticipatorsEntity() {
                this(false);

    }

    public String getEmail() {
        return email;
    }
    @OneToOne
    private ParticipantListEntity participantList;

    public ParticipantListEntity getParticipantList() {
        return participantList;
    }

    public void setParticipantList(ParticipantListEntity participantList) {
        this.participantList = participantList;
    }

}
