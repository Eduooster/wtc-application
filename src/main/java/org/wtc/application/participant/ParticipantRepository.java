package org.wtc.application.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.user.entity.User;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {






    Optional<Participant> findByParticipantType(ParticipantType participantType);
}
