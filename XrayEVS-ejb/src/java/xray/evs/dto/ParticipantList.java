/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abhinavralhan
 */
public class ParticipantList extends AbstractDTO {

    private static final long serialVersionUID = 8567213270692926184L;
    
    private String name;

    private List<ListParticipators> participants;
    
    public ParticipantList() {
        participants = new ArrayList<>();
    }
    
    public ParticipantList(String uuid, int jpaVersion, String name, List<ListParticipators> participants) {
        super(uuid, jpaVersion);
        this.name = name;
        this.participants = participants;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<ListParticipators> getParticipants() {
        return participants;
    }
    
    public void setParticipants(List<ListParticipators> participants) {
        this.participants = participants;
    }
    
    public boolean removeParticipant(ListParticipators participant) {
        if (participant.getUuid() != null) {
            return participants.remove(participant);
        } else {
            String participantToDelete = participant.getEmail();
            for (int i = 0; i < participants.size(); i++) {
                if (participants.get(i).getEmail().equals(participantToDelete)) {
                    participants.remove(i);
                    return true;
                }
            }
            return false;
        }
    }
    
    public void addParticipant(ListParticipators participant) {
        participants.add(participant);
    }
    
}
