/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

/**
 *
 * @author Ahmad
 */
public class Token extends AbstractDTO {

    private static final long serialVersionUID = -3543454844012789119L;

    private String token;
    private Participant participant;
    private Poll poll;

    public Token() {
    }

    public Token(String uuid, int jpaVersion, String token, Participant participant, Poll poll ) {
        super(uuid, jpaVersion);
        this.token = token;
        this.participant = participant;
        this.poll = poll;
    }

    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
    
    
}

