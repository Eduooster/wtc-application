package org.wtc.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ParticipantRepository participantRepository;

    @Override
    @Transactional
    public void run(String... args) {

        participantRepository.findByParticipantType(ParticipantType.SYSTEM)
                .orElseGet(() -> {
                    Participant system = new Participant();
                    system.setParticipantType(ParticipantType.SYSTEM);
                    return participantRepository.save(system);
                });

    }
}