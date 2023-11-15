package xray.evs.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import xray.evs.entities.ParticipantListEntity;
import xray.evs.entities.PollEntity;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-17T10:23:03", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(OrganizerEntity.class)
public class OrganizerEntity_ extends AbstractEntity_ {

    public static volatile SingularAttribute<OrganizerEntity, String> firstName;
    public static volatile SingularAttribute<OrganizerEntity, String> lastName;
    public static volatile ListAttribute<OrganizerEntity, ParticipantListEntity> participentLists;
    public static volatile ListAttribute<OrganizerEntity, PollEntity> polls;
    public static volatile SingularAttribute<OrganizerEntity, String> email;

}