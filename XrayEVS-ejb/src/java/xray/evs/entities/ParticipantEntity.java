package xray.evs.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 *
 * @author Danoosh
 */
@Entity
public class ParticipantEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -6093396122211624885L;

    private String email;

    @ManyToMany(mappedBy = "participants")
    private List<PollEntity> poll;

    public ParticipantEntity() {
        this(false);
    }

    public ParticipantEntity(boolean generate) {
        super(generate);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public List<PollEntity> getPoll() {
        return poll;
    }

    public void setPoll(List<PollEntity> poll) {
        this.poll = poll;
    }

   

}
