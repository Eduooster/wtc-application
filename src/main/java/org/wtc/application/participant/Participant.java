package org.wtc.application.participant;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.user.entity.User;

import java.util.Objects;

@Entity(name = "wtc_participants")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ParticipantType participantType;
    private Long refId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;

        return Objects.equals(this.refId, that.refId) &&
                this.participantType == that.participantType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(refId, participantType);
    }


}