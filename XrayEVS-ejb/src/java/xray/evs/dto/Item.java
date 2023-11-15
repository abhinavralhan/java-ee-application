/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

/**
 *
 * @author Abhinav
 */
public class Item extends AbstractDTO {

    private static final long serialVersionUID = -979582917212833843L;

    private String shortName;
    private String desc;
    private Integer vote;
    private boolean participantVote;

    public Item() {
    }

    public Item(String uuid, int jpaVersion, String shortName, String description, Integer votes) {
        super(uuid, jpaVersion);
        this.shortName = shortName;
        this.desc = description;
        this.vote = votes;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    public Integer getVotes() {
        return vote;
    }

    public void setVotes(Integer votes) {
        this.vote = votes;
    }

    public boolean isParticipantVote() {
        return participantVote;
    }

    public void setParticipantVote(boolean participantVote) {
        this.participantVote = participantVote;
    }

}
