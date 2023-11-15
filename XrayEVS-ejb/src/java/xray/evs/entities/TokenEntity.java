package xray.evs.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author Abhinav
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getTokenValue",
            query = "SELECT c FROM TokenEntity c"
            + " WHERE c.tokenValue = :tokenValue "
    ),
    @NamedQuery(
            name = "getTokenByUuid",
            query = "SELECT c FROM TokenEntity c"
            + " WHERE c.uuid = :uuid "
    ),
    @NamedQuery(
            name = "deleteTokenByUuid",
            query = "DELETE FROM TokenEntity c"
            + " WHERE c.uuid = :uuid"
    )})
public class TokenEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 5954700222064336428L;

    private String tokenValue;

    @ManyToOne
    private PollEntity poll;

    @OneToOne
    private ParticipantEntity participant;

    public TokenEntity() {
        super(false);
    }

    public TokenEntity(boolean newEntity) {
        super(newEntity);
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public PollEntity getPoll() {
        return poll;
    }

    public void setPoll(PollEntity poll) {
        this.poll = poll;
    }

    public ParticipantEntity getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantEntity participant) {
        this.participant = participant;
    }

}
