package org.wtc.application.segment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class SegmentValidationService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public void validateOperatorForConversation(AuthenticableUser authenticatedUser, Conversation conversation) {

        User operator = userRepository.findByCredentials(authenticatedUser)
                .orElseThrow(() -> new EntityNotFoundException("Operator user not found"));


        Participant clientParticipant = conversation.getParticipants()
                .stream()
                .filter(p -> p.getParticipantType() == ParticipantType.CLIENT)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Client participant not found in this conversation"));


        Client client = clientRepository.findById(clientParticipant.getRefId())
                .orElseThrow(() -> new EntityNotFoundException("Client business entity not found"));


        this.validate(operator, client);
    }

    public void validateByIds(Long operatorId, Long clientId) {
        User operator = userRepository.findById(operatorId)
                .orElseThrow(() -> new EntityNotFoundException("Operador não encontrado"));

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        validate(operator, client);
    }


    public void validate(User operator, Client client) {
        boolean hasCompatibleSegment = operator.getSegments()
                .stream()
                .anyMatch(segment -> client.getSegments().contains(segment));

        if (!hasCompatibleSegment) {
            throw new AccessDeniedException(
                    "Operator does not have compatible segments to answer this client"
            );
        }
    }
}