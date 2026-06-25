package org.wtc.application.conversation.service.useCases;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.segment.service.ConversationAcessService;

@Service
@RequiredArgsConstructor
public class JoinConversation {

    private final ConversationRepository conversationRepository;

    private final ConversationAcessService conversationAcessService;



    @Transactional
    public void joinConversation(Long conversationId, AuthenticableUser authenticatedUser) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));


        conversationAcessService.validateOperatorCanJoinConversation(authenticatedUser, conversation);

        conversation.getParticipants().add(authenticatedUser.getParticipant());

        conversationRepository.save(conversation);
    }
}
