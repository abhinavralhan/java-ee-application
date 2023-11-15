/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.logic;
import xray.evs.dto.Poll;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author abhinavralhan
 */
@Remote
public interface AdministratorLogic {
    public static final String ADMINISTRATOR_ROLE = "ADMIN";

    public abstract List<Poll> getPollList();

    public abstract boolean deletePoll(String uuid);
}
