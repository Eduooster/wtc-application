package org.wtc.application.segment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.participant.Participant;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;
@Service
@RequiredArgsConstructor
public class ConversationAcessService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final SegmentValidationService segmentValidationService;

    public void validateOperatorCanJoinConversation(AuthenticableUser authenticatedUser, Conversation conversation) {


        User operator = userRepository.findByCredentials(authenticatedUser)
                .orElseThrow(() -> new EntityNotFoundException("Operator user not found"));

        Participant clientParticipant = conversation.getParticipants()
                .stream()
                .filter(p -> p.getParticipantType() == ParticipantType.CLIENT)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Client participant not found in this conversation"));

        Client client = clientRepository.findByParticipant(clientParticipant)
                .orElseThrow(() -> new EntityNotFoundException("Client business entity not found"));

        segmentValidationService.ensureCompatibleSegments(operator, client);
    }
}
