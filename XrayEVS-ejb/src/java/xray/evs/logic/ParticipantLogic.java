/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.logic;

import xray.evs.dto.Poll;
import xray.evs.dto.Token;

/**
 *
 * @author abhinavralhan
 */
public interface ParticipantLogic {

    public Token getByToken(String token);
    
    public boolean submitPoll(Token token);
    
    public Poll getPollByResultToken(String resultToken);
}
