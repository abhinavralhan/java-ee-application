package xray.evs.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Danoosh
 */
@NamedQueries({
    @NamedQuery(
            name = "getParticipantLists",
            query = "SELECT c FROM ParticipantListEntity c"
            + " WHERE c.organizer = :organizer"
    ),
    @NamedQuery(
            name = "getParticipantListByUuid",
            query = "SELECT c FROM ParticipantListEntity c"
            + " WHERE c.organizer = :organizer AND c.uuid = :uuid"
    ),
    @NamedQuery(
            name = "deleteParticipantListByUuid",
            query = "DELETE FROM ParticipantListEntity c WHERE c.uuid = :uuid"
    )
})

@Entity
@Table(uniqueConstraints
        = @UniqueConstraint(columnNames = {"organizer_id", "name"}))
public class ParticipantListEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -7696436423171058587L;

    private String name;

    public ParticipantListEntity() {
         this(false); }

    public ParticipantListEntity(boolean newEntity) {
        super(newEntity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "participantList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private List<ListParticipatorsEntity> participants;
    
    @ManyToOne
    private OrganizerEntity organizer;

    public OrganizerEntity getOrganizer() {
        return organizer;
    }

    public void setOrganizer(OrganizerEntity organizer) {
        this.organizer = organizer;
    }

    public List<ListParticipatorsEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ListParticipatorsEntity> participants) {
        this.participants = participants;
    }


}
