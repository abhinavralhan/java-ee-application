/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package xray.evs.utilities;

/**
 *
 * @author Ahmad
 */
public enum PollState {
    PREPARED, STARTED, VOTING, FINISHED;

    public String getName() {
        return this.name();
    }
}
