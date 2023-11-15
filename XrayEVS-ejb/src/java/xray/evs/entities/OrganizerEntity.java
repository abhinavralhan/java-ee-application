package xray.evs.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Danoosh
 */
@NamedQueries({
    @NamedQuery(
            name = "getOrganizerByEmail",
            query = "SELECT o FROM OrganizerEntity o WHERE o.email = :email"
    )
})
@Entity
public class OrganizerEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 2145990854904618778L;

    private String firstName;
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    
    @ManyToMany(mappedBy = "organizers")
    private List<PollEntity> polls;
    
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private List<ParticipantListEntity> participentLists;

    public OrganizerEntity() {
        this(false);
    }

    public OrganizerEntity(boolean newEntity) {
        super(newEntity);
        if (newEntity) {
            participentLists = new ArrayList<>();
            polls = new ArrayList<>();
        }
    }

    public List<PollEntity> getPolls() {
        return polls;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPolls(List<PollEntity> polls) {
        this.polls = polls;
    }

    public List<ParticipantListEntity> getParticipentLists() {
        return participentLists;
    }

    public void setParticipentLists(List<ParticipantListEntity> participentLists) {
        this.participentLists = participentLists;
    }

}
