/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Ahmad
 */

@NamedQueries({
    @NamedQuery(
            name = "getItemByUuid",
            query = "SELECT c FROM ItemEntity c"
            + " WHERE c.uuid = :uuid"
    )
})
@Entity
public class ItemEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -4476472521868028071L;
    
    
    private String shortName;
    private String description;  
    private Integer votesCounter;
    
    @ManyToOne
    private QuestionEntity question;
    
    
    
    public Integer getVotesCounter() {
        return votesCounter;
    }

    public void setVotesCounter(Integer votesCounter) {
        this.votesCounter = votesCounter;
    }

    
  

    public ItemEntity() {
        super(false);
    }
    
    public ItemEntity(boolean newEntity) {
        super(newEntity);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }
    

}
