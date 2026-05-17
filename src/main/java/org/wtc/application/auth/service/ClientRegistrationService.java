package org.wtc.application.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.wtc.application.auth.dto.ClientRegistrationDto;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.auth.entity.RoleProfile;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.participant.Participant;

@Service
@RequiredArgsConstructor
public class ClientRegistrationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public void register(ClientRegistrationDto dto) {
        var credentials = new AuthenticableUser();
        credentials.setEmail(dto.email());
        credentials.setPassword(passwordEncoder.encode(dto.password()));
        credentials.setRole(RoleProfile.CLIENT);

        var participant = new Participant();
        participant.setParticipantType(ParticipantType.CLIENT);

        credentials.setParticipant(participant);


        var client = new Client();
        client.setCompanyName(dto.companyName());
        client.setPhoneNumber(dto.phoneNumber());
        client.setCredentials(credentials);
        client.setFullName(dto.fullName());
        client.setParticipant(participant);





        clientRepository.save(client);
    }
}