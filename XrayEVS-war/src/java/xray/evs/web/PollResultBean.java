/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xray.evs.web;

import xray.evs.dto.Item;
import xray.evs.dto.Poll;
import xray.evs.dto.Question;
import xray.evs.utilities.PollState;
import xray.evs.logic.OrganizerLogic;
import xray.evs.logic.ParticipantLogic;
import evs.web.i18n.Messages;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Team Xray
 */
@ViewScoped
@Named
public class PollResultBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrganizerLogic ol;

    @EJB
    private ParticipantLogic pl;

    private String uuid;
    private Poll poll;

    private String token;
    private Poll publishedPoll;

    private boolean success;
    private boolean tempPublished;

    public boolean isValid() {
        return uuid != null && getPoll() != null && (poll.getPollState() == PollState.FINISHED);
    }

    public Poll getPoll() {
        if (poll == null) {
            poll = ol.getPoll(uuid);
            tempPublished = false;

            if (poll != null && !(poll.getPollState() == PollState.FINISHED)) {
                poll = null;
            }
        }

        return poll;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Poll getPublishedPoll() {

        if (publishedPoll == null) {
            publishedPoll = pl.getPollByResultToken(token);
            if (publishedPoll != null && publishedPoll.getPollState() != PollState.FINISHED) {
                publishedPoll = null;
            }
        }
        return publishedPoll;
    }

    public boolean isPublishValid() {
        System.out.println("token is: " + token);
        System.out.println("getPub is: " + getPublishedPoll());
        System.out.println("pollS is: " + publishedPoll.getPollState());

        var b = token != null && getPublishedPoll() != null && publishedPoll.getPollState() == PollState.FINISHED;
        System.out.println("return is: " + b);
        return b;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean hasEnoughVotes() {
        return poll.getVoteCounter() >= 3;
    }

    public void deletePoll() {
        ol.deletePoll(uuid);
    }

    public void publishPoll() {
        if (hasEnoughVotes() && poll.getPollState() == PollState.FINISHED && !tempPublished) {
            tempPublished = true;
            uuid = ol.publishPoll(uuid);

            if (uuid != null) {
                success = true;
                poll = null;
            }
        } else {
            System.out.print("Not enough votes to publish Poll or already published");
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Item> getResultOfQuestion(Question question) {
        List<Item> resultList = new ArrayList<>();
        Integer absentees = question.getAbstentions();
        if (absentees > 0) {
            Item absentItem = new Item();
            absentItem.setShortName(Messages.getMessage("questionAbsentionLabel"));
            absentItem.setVotes(absentees);
            resultList.add(absentItem);
        }
        resultList.addAll(question.getItems());
        return resultList;
    }

    public String getPercentage(Integer votesForThisItem, Integer totalVotes) {
        float percentage = (1.0f * votesForThisItem / totalVotes) * 100;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        return df.format(percentage);
    }

}
