/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

import xray.evs.utilities.PollState;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ahmad
 */
@XmlRootElement

public class Poll extends AbstractDTO {


    private String title;
    private Date startDate;
    private Date endDate;
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private PollState pollState;

    private String resultToken;
    private Integer voteCounter;
    private Boolean trackingIsEnabled;

    private List<Organizer> organizer;
    private List<Question> questions;
    private List<Participant> participants;

    public Poll() {
        this.voteCounter = 0;
        this.trackingIsEnabled = false;
        this.participants = new ArrayList<>();
        this.questions = new ArrayList<>();
    }

    public Poll(String uuid, int jpaVersion, String title, Date startDate, Date endDate, String description, PollState pollState, String resultToken, Integer voteCounter, Boolean trackingIsEnabled, List<Organizer> organizer, List<Question> questions, List<Participant> participants) {
        super(uuid, jpaVersion);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.pollState = pollState;
        this.resultToken = resultToken;
        this.voteCounter = voteCounter;
        this.trackingIsEnabled = trackingIsEnabled;
        this.organizer = organizer;
        this.questions = questions;
        this.participants = participants;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getTrackingIsEnabled() {
        return trackingIsEnabled;
    }

    public void setTrackingIsEnabled(Boolean trackingIsEnabled) {
        this.trackingIsEnabled = trackingIsEnabled;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PollState getPollState() {
        return pollState;
    }

    public void setPollState(PollState pollState) {
        this.pollState = pollState;
    }

    public String getResultToken() {
        return resultToken;
    }

    public void setResultToken(String resultToken) {
        this.resultToken = resultToken;
    }

    public List<Organizer> getOrganizer() {
        return organizer;
    }

    public void setOrganizer(List<Organizer> organizer) {
        this.organizer = organizer;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public Integer getVoteCounter() {
        return voteCounter;
    }

    public void setVoteCounter(Integer voteCounter) {
        this.voteCounter = voteCounter;
    }

    public boolean addParticipant(Participant participant) {
        return participants.add(participant);
    }

    public boolean removeParticipant(Participant participant) {
        if (participant.getUuid() != null) {
            return participants.remove(participant);
        } else {
            String emailToDelete = participant.getEmail();
            for (int i = 0; i < participants.size(); i++) {
                if (participants.get(i).getEmail().equals(emailToDelete)) {
                    participants.remove(i);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean addQuestion(Question question) {
        return questions.add(question);
    }

    public boolean removeQuestion(Question question) {
        if (question.getUuid() != null) {
            return questions.remove(question);
        } else {
            String questionToDelete = question.getTitle();
            for (int i = 0; i < questions.size(); i++) {
                if (questions.get(i).getTitle().equals(questionToDelete)) {
                    questions.remove(i);
                    return true;
                }
            }
            return false;
        }
    }
}
