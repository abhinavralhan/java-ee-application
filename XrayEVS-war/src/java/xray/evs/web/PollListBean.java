/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xray.evs.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import xray.evs.dto.Item;
import xray.evs.dto.MultipleChoiceQuestion;
import xray.evs.dto.Participant;
import xray.evs.dto.Poll;
import xray.evs.dto.Question;
import xray.evs.utilities.PollState;
import xray.evs.utilities.QuestionType;
import xray.evs.logic.OrganizerLogic;
import java.time.LocalDateTime;

/**
 * 
 * @author Team Xray
 */
@RequestScoped
@Named
public class PollListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrganizerLogic ol;

    private List<Poll> pollList;
    
    public List<Poll> getPollList() {
        if (pollList == null) {
            pollList = ol.getPollList();
        }
        return pollList;
    }

    public void deletePoll(Poll poll) {
        ol.deletePoll(poll.getUuid());
        pollList = ol.getPollList();
    }
}
