/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xray.evs.web;

import xray.evs.dto.Poll;
import xray.evs.logic.AdministratorLogic;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Admin
 */
@RequestScoped
@Named
public class AdminBean implements Serializable {

    private static final long serialVersionUID  = 1L;

    @EJB
    private AdministratorLogic al;

    private List<Poll> pollList;

    public List<Poll> getPollList() {
        if (pollList == null) {
            pollList = al.getPollList();
        }
        return pollList;
    }

    public void deletePoll(Poll poll) {
        al.deletePoll(poll.getUuid());
        pollList = al.getPollList();
    }
}
