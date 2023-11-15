package xray.evs.entities;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import xray.evs.entities.OrganizerEntity;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.QuestionEntity;
import xray.evs.entities.TokenEntity;
import xray.evs.utilities.PollState;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-17T10:23:03", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(PollEntity.class)
public class PollEntity_ extends AbstractEntity_ {

    public static volatile SingularAttribute<PollEntity, String> resultToken;
    public static volatile SingularAttribute<PollEntity, LocalDateTime> endDate;
    public static volatile SingularAttribute<PollEntity, PollState> pollState;
    public static volatile ListAttribute<PollEntity, QuestionEntity> questions;
    public static volatile ListAttribute<PollEntity, OrganizerEntity> organizers;
    public static volatile SingularAttribute<PollEntity, Integer> voteCounter;
    public static volatile SingularAttribute<PollEntity, Boolean> trackingIsEnabled;
    public static volatile ListAttribute<PollEntity, TokenEntity> tokens;
    public static volatile SingularAttribute<PollEntity, String> title;
    public static volatile SingularAttribute<PollEntity, String> describtion;
    public static volatile SingularAttribute<PollEntity, LocalDateTime> startDate;
    public static volatile ListAttribute<PollEntity, ParticipantEntity> participants;

}