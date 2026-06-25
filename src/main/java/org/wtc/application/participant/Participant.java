package org.wtc.application.participant;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.user.entity.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @ManyToMany(mappedBy = "participants")
    private Set<Conversation> conversations = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant that = (Participant) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }




}