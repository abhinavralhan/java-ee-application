/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

/**
 *
 * @author abhinavralhan
 */

public class Participant extends AbstractDTO {

    private static final long serialVersionUID = 7587984175401214362L;
    
    private String email;

    public Participant() {
    }
    
    public Participant(String uuid, int jpaVersion, String email) {
        super(uuid, jpaVersion);
        this.email = email.toLowerCase();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
    
}
