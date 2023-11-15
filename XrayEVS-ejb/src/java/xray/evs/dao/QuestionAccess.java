/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dao;

import java.util.List;
import java.util.stream.Collectors;
import xray.evs.dto.Question;
import xray.evs.entities.PollEntity;
import xray.evs.entities.QuestionEntity;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import xray.evs.dto.MultipleChoiceQuestion;
import xray.evs.entities.MultipleChoiceQuestionEntity;
import xray.evs.utilities.QuestionType;

/**
 *
 * @author Ahmad
 */
@LocalBean
@Stateless

public class QuestionAccess {

    @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;

    @EJB
    private ItemAccess itemAccess;

    public QuestionEntity createQuestion(Question question, PollEntity entity) {
        if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            MultipleChoiceQuestionEntity qe = new MultipleChoiceQuestionEntity(true);
            qe.setQuestionType(question.getQuestionType());
            qe.setTitle(question.getTitle());
            qe.setPoll(entity);
            qe.setMinChoices(((MultipleChoiceQuestion) question).getMinChoices());
            qe.setMaxChoices(((MultipleChoiceQuestion) question).getMaxChoices());
            qe.setVotes(0);
            qe.setAbstentions(0);
            em.persist(qe);
            qe.setItems(itemAccess.createItem(question.getItems(), qe));
            return qe;
        } else {
            QuestionEntity qe = new QuestionEntity(true);
            qe.setQuestionType(question.getQuestionType());
            qe.setTitle(question.getTitle());
            qe.setPoll(entity);
            qe.setVotes(0);
            qe.setAbstentions(0);
            em.persist(qe);
            qe.setItems(itemAccess.createItem(question.getItems(), qe));
            return qe;
        }
    }

    public List<QuestionEntity> createQuestion(List<Question> questions, PollEntity pollEntity) {
        return questions.stream().map(n -> createQuestion(n, pollEntity)).collect(Collectors.toList());
    }

    public QuestionEntity getQuestionByUuid(String uuid) {
        try {
            QuestionEntity ce = em.createNamedQuery("getQuestionByUuid", QuestionEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
            return ce;
        } catch (NoResultException e) {
            return null;
        }
    }
}
