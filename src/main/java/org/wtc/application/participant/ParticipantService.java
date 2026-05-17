package org.wtc.application.participant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.auth.entity.RoleProfile;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ParticipantRepository participantRepository;

    public Participant resolve(AuthenticableUser auth) {

        if (auth.getRole() == RoleProfile.OPERATOR) {

            User user = userRepository.findByCredentials(auth)
                    .orElseThrow();

            return participantRepository.findByParticipantTypeAndRefId(
                    ParticipantType.OPERATOR,
                    user.getId()
            ).orElseThrow();
        }

        Client client = clientRepository.findByCredentials(auth)
                .orElseThrow();

        return participantRepository.findByParticipantTypeAndRefId(
                ParticipantType.CLIENT,
                client.getId()
        ).orElseThrow();
    }
}
