package xray.evs.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import xray.evs.entities.ListParticipatorsEntity;
import xray.evs.entities.OrganizerEntity;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-17T10:23:03", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(ParticipantListEntity.class)
public class ParticipantListEntity_ extends AbstractEntity_ {

    public static volatile SingularAttribute<ParticipantListEntity, OrganizerEntity> organizer;
    public static volatile SingularAttribute<ParticipantListEntity, String> name;
    public static volatile ListAttribute<ParticipantListEntity, ListParticipatorsEntity> participants;

}