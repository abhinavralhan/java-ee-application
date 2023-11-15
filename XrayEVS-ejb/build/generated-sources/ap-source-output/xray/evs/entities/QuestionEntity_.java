package xray.evs.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import xray.evs.entities.ItemEntity;
import xray.evs.entities.PollEntity;
import xray.evs.utilities.QuestionType;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-17T10:23:03", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(QuestionEntity.class)
public class QuestionEntity_ extends AbstractEntity_ {

    public static volatile SingularAttribute<QuestionEntity, Integer> votes;
    public static volatile SingularAttribute<QuestionEntity, PollEntity> poll;
    public static volatile SingularAttribute<QuestionEntity, String> title;
    public static volatile SingularAttribute<QuestionEntity, QuestionType> questionType;
    public static volatile SingularAttribute<QuestionEntity, Integer> abstentions;
    public static volatile ListAttribute<QuestionEntity, ItemEntity> items;

}