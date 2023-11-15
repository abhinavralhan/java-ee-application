/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dto;

import xray.evs.utilities.QuestionType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ahmad
 */
@XmlRootElement

public class Question extends AbstractDTO {

    private static final long serialVersionUID = 2145740826632325263L;

    private String title;

    @Enumerated(EnumType.ORDINAL)
    private QuestionType questionType;

    private Integer votes;
    private Integer abstentions;
    private boolean absent;

    public boolean isAbsent() {
        return absent;
    }

    public void setAbsent(boolean absent) {
        this.absent = absent;
    }

    private List<Item> items;

    public Question() {
        items = new ArrayList<>();
    }

    public Question(String uuid, int jpaVersion, String title, QuestionType questionType, Integer votes, Integer abstentions, List<Item> items) {
        super(uuid, jpaVersion);
        this.title = title;
        this.questionType = questionType;
        this.votes = votes;
        this.abstentions = abstentions;
        this.items = items;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getAbstentions() {
        return abstentions;
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

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    
    public boolean addItem(Item item) {
        return items.add(item);
    }

}
