package xray.evs.entities;

import javax.persistence.Entity;

import xray.evs.utilities.QuestionType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Abhinav
 */

@NamedQueries({
    @NamedQuery(
            name = "getQuestionByUuid",
            query = "SELECT q FROM QuestionEntity q"
            + " WHERE q.uuid = :uuid"
    )
})

@Entity
@Table(uniqueConstraints
        = @UniqueConstraint(columnNames = {"poll_id", "title"}))
@Inheritance(strategy = InheritanceType.JOINED)
public class QuestionEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -2762675751339561563L;
    private String title;

    @Enumerated(EnumType.ORDINAL)
    private QuestionType questionType;

    private Integer votes;
    private Integer abstentions;


    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> items;

    @OneToOne
    private PollEntity poll; 

    public QuestionEntity() {
        super(false);
    }

    public QuestionEntity (boolean newEntity) {
        super(newEntity);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public Integer getAbstentions() {
        return abstentions;
    }

    public void setAbstentions(Integer abstentions) {
        this.abstentions = abstentions;
    }
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }




    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    public PollEntity getPoll() {
        return poll;
    }

    public void setPoll(PollEntity poll) {
        this.poll = poll;
    }
    
    
    
}
