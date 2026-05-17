package org.wtc.application.participant;

import jakarta.persistence.*;
import lombok.Data;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.user.entity.User;

@Entity
@Table(name = "wtc_participant")
@Data

public class Participant {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ParticipantType participantType;
    private Long refId;


}