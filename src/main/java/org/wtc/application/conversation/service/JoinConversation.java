package org.wtc.application.conversation.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;
import org.wtc.application.segment.service.SegmentValidationService;

@Service
@RequiredArgsConstructor
public class JoinConversation {

    private final ConversationRepository conversationRepository;
    private final SegmentValidationService segmentValidationService;



    @Transactional
    public void joinConversation(Long conversationId, AuthenticableUser authenticatedUser) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));


        segmentValidationService.validateOperatorForConversation(authenticatedUser, conversation);

        conversation.getParticipants().add(authenticatedUser.getParticipant());

        conversationRepository.save(conversation);
    }
}
