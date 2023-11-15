/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

/**
 *
 * @author abhinavralhan
 */

public class ListParticipators extends AbstractDTO {

    private static final long serialVersionUID = 6521766067804595782L;

    
    private String email;
    
    public ListParticipators() {
    }
    
    public ListParticipators(String uuid, int jpaVersion, String email) {
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
