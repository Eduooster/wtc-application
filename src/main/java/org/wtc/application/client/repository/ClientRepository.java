package org.wtc.application.client.repository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.participant.Participant;
import org.wtc.application.segment.entity.Segment;

import java.nio.channels.FileChannel;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByIdAndDeletedFalse(Long id);

    List<Client> findAllByDeletedFalse();

    Optional<Client> findByCredentials(AuthenticableUser authenticableUser);

    List<Client> findAllBySegments(Segment segment);


    Optional<Client> findByParticipant(Participant clientParticipant);
}
