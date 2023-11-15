/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import xray.evs.utilities.QuestionType;

/**
 *
 * @author Ahmad
 */
@XmlRootElement
public class MultipleChoiceQuestion extends Question {

    private Integer minChoices;
    private Integer maxChoices;

    public MultipleChoiceQuestion() {
        super();
    }

    public MultipleChoiceQuestion(String uuid, int jpaVersion, String title, List<Item> items, Integer absentions, Integer votes, Integer minChoices, Integer maxChoices) {
        super(uuid, jpaVersion, title, QuestionType.YES_NO, votes, absentions, items);
        this.minChoices = minChoices;
        this.maxChoices = maxChoices;
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
