/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

/**
 *
 * @author abhinavralhan
 */
public class Organizer extends AbstractDTO {

    private static final long serialVersionUID = -8078550688480266404L;
    
    private String email;
    private String firstName;
    private String lastName;
    
    public Organizer() {

    }
    
    public Organizer(String uuid, int jpaVersion, String email, String firstName, String lastName) {
        super(uuid, jpaVersion);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
