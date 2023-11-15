package xray.evs.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.PollEntity;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-17T10:23:03", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(TokenEntity.class)
public class TokenEntity_ extends AbstractEntity_ {

    public static volatile SingularAttribute<TokenEntity, PollEntity> poll;
    public static volatile SingularAttribute<TokenEntity, String> tokenValue;
    public static volatile SingularAttribute<TokenEntity, ParticipantEntity> participant;

}