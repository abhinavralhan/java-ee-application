package xray.evs.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-17T10:23:03", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(MultipleChoiceQuestionEntity.class)
public class MultipleChoiceQuestionEntity_ extends QuestionEntity_ {

    public static volatile SingularAttribute<MultipleChoiceQuestionEntity, Integer> maxChoices;
    public static volatile SingularAttribute<MultipleChoiceQuestionEntity, Integer> minChoices;

}