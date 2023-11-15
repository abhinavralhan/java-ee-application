/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.entities;

import xray.evs.utilities.PollState;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Ahmad
 */
@NamedQueries({
    @NamedQuery(
            name = "getPollByUuid",
            query = "SELECT c FROM PollEntity c"
            + " WHERE :organizer MEMBER OF c.organizers AND c.uuid = :uuid"
    ),
    @NamedQuery(
            name = "getPollByUuidAdmin",
            query = "SELECT c FROM PollEntity c"
            + " WHERE c.uuid = :uuid"
            + " ORDER BY c.title"
    ),
    @NamedQuery(
            name = "getAllPolls",
            query = "SELECT c FROM PollEntity c"
            + " ORDER BY c.title"
    ),
    @NamedQuery(
            name = "getPollList",
            query = "SELECT c FROM PollEntity c"
            + " WHERE c.organizers = :organizers"
            + " ORDER BY c.title"
    ),
    @NamedQuery(
            name = "getPollByResultToken",
            query = "SELECT c FROM PollEntity c"
            + " WHERE c.resultToken = :resulttoken"
    ),
    @NamedQuery(
            name = "getListOfPollTitle",
            query = "SELECT c.title FROM PollEntity c")
})

@Entity
public class PollEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 9019420001573709345L;

    @Column(unique = true)
    private String title;

    private String describtion;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String resultToken;
    private Integer voteCounter;
    private Boolean trackingIsEnabled;

    @Enumerated(EnumType.ORDINAL)
    private PollState pollState;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenEntity> tokens;

    @ManyToMany
    @JoinTable(
            name = "poll_organizer",
            joinColumns = @JoinColumn(name = "poll_id"),
            inverseJoinColumns = @JoinColumn(name = "organizer_id")
    )
    private List<OrganizerEntity> organizers;

    @ManyToMany
    @JoinTable(
            name = "poll_participant",
            joinColumns = @JoinColumn(name = "poll_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<ParticipantEntity> participants;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions;

    public PollEntity() {
        this(false);
    }

    public PollEntity(boolean newEntity) {
        super(newEntity);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public PollState getPollState() {
        return pollState;
    }

    public void setPollState(PollState pollState) {
        this.pollState = pollState;
    }

    public List<ParticipantEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantEntity> participants) {
        this.participants = participants;
    }

    public List<TokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenEntity> tokens) {
        this.tokens = tokens;
    }

    public List<OrganizerEntity> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<OrganizerEntity> organizers) {
        this.organizers = organizers;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }

    public String getDescription() {
        return describtion;
    }

    public void setDescription(String describtion) {
        this.describtion = describtion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getResultToken() {
        return resultToken;
    }

    public void setResultToken(String resultToken) {
        this.resultToken = resultToken;
    }

    public Integer getVoteCounter() {
        return voteCounter;
    }

    public void setVoteCounter(Integer voteCounter) {
        this.voteCounter = voteCounter;
    }

    public Boolean getTrackingIsEnabled() {
        return trackingIsEnabled;
    }

    public void setTrackingIsEnabled(Boolean trackingIsEnabled) {
        this.trackingIsEnabled = trackingIsEnabled;
    }

}
