/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.entities;

import javax.persistence.Entity;
import xray.evs.utilities.QuestionType;

/**
 *
 * @author Ahmad
 */
@Entity
public class MultipleChoiceQuestionEntity extends QuestionEntity {

    private static final long serialVersionUID = -5834168557016161570L;

    private Integer minChoices;
    private Integer maxChoices;

   
    public MultipleChoiceQuestionEntity() {
        super(false);
        super.setQuestionType(QuestionType.MULTIPLE_CHOICE);
    }

   
    public MultipleChoiceQuestionEntity(boolean newEntity) {
        super(newEntity);
        super.setQuestionType(QuestionType.MULTIPLE_CHOICE);
    }

   
    public Integer getMinChoices() {
        return minChoices;
    }

   
    public void setMinChoices(Integer minChoices) {
        this.minChoices = minChoices;
    }

    
    public Integer getMaxChoices() {
        return maxChoices;
    }

   
    public void setMaxChoices(Integer maxChoices) {
        this.maxChoices = maxChoices;
    }
    
}
