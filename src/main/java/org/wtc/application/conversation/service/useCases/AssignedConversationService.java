package org.wtc.application.conversation.service.useCases;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.exceptions.ConversationAlreadyAssignedException;
import org.wtc.application.conversation.exceptions.InvalidConversationStatusException;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignedConversationService {

    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Transactional
    public Conversation assignOperator(Long conversationId, AuthenticableUser authenticatedUser) {



        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));

        validateOperatorCanAssign(conversation, authenticatedUser);


        Participant operatorParticipant = participantRepository
                .findById(authenticatedUser.getParticipant().getId())
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));


        conversation.getParticipants().add(operatorParticipant);


        conversation.setAssignedOperator(operatorParticipant);
        conversation.setStatus(ConversationStatus.IN_PROGRESS);

        return conversationRepository.saveAndFlush(conversation);
    }

    public void validateOperatorCanAssign(Conversation conversation, AuthenticableUser authenticableUser) {
        if (conversation.getAssignedOperator() != null) {
            throw new ConversationAlreadyAssignedException("Conversation already assigned to an operator");
        }

        if (conversation.getStatus() != ConversationStatus.WAITING_OPERATOR) {
            throw new InvalidConversationStatusException("Conversation is not available for assignment");
        }



        validateSegmentCompatibility(conversation, authenticableUser);
    }

    private void validateSegmentCompatibility(
            Conversation conversation,
            AuthenticableUser authenticatedUser) {

        User operator = userRepository.findByCredentials(authenticatedUser)
                .orElseThrow(() -> new EntityNotFoundException("Operator user not found"));

        Participant clientParticipant = conversation.getParticipants()
                .stream()
                .filter(p -> p.getParticipantType() == ParticipantType.CLIENT)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Client participant not found in this conversation"));

        Client client = clientRepository.findByParticipant(clientParticipant)
                .orElseThrow(() -> new EntityNotFoundException("Client business entity not found"));

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