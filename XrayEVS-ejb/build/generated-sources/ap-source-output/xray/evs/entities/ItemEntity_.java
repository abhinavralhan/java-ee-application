package xray.evs.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import xray.evs.entities.QuestionEntity;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-17T10:23:03", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(ItemEntity.class)
public class ItemEntity_ extends AbstractEntity_ {

    public static volatile SingularAttribute<ItemEntity, QuestionEntity> question;
    public static volatile SingularAttribute<ItemEntity, String> description;
    public static volatile SingularAttribute<ItemEntity, Integer> votesCounter;
    public static volatile SingularAttribute<ItemEntity, String> shortName;

}