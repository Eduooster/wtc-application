package org.wtc.application.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.wtc.application.auth.dto.UserRegistrationDto;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.auth.entity.RoleProfile;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.participant.Participant;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void register(UserRegistrationDto dto) {
        var credentials = new AuthenticableUser();
        credentials.setEmail(dto.email());
        credentials.setPassword(passwordEncoder.encode(dto.password()));
        credentials.setRole(RoleProfile.OPERATOR);

        var participant = new Participant();
        participant.setParticipantType(ParticipantType.OPERATOR);

        credentials.setParticipant(participant);


        var user = new User();
        user.setFullName(dto.name());
        user.setCredentials(credentials);
        user.setFullName(dto.name());
        user.setParticipant(participant);



        userRepository.save(user);
    }
}