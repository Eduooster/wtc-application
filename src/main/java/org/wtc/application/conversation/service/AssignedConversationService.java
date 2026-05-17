package org.wtc.application.conversation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;

@Service
@RequiredArgsConstructor
public class AssignedConversationService {

    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Conversation assignOperator(
            Long conversationId,
            AuthenticableUser authenticatedUser
    ) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Participant participant = participantRepository.findByParticipantTypeAndRefId(authenticatedUser.getParticipant().getParticipantType(), authenticatedUser.getParticipant().getRefId()).orElseThrow(() -> new RuntimeException("Participant not found"));

        if (conversation.getAssignedOperator() != null) {
            throw new RuntimeException("Conversation already assigned");
        }

        conversation.setAssignedOperator(participant);

        conversation.getParticipants().add(participant);

        conversation.setStatus(ConversationStatus.IN_PROGRESS);

        return conversationRepository.save(conversation);
    }





}
