package org.wtc.application.conversation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.participant.Participant;

@Service
@RequiredArgsConstructor
public class ValidateAcessConversationService {

    private final ConversationRepository conversationRepository;



    private Conversation validateAccess(Long conversationId, Participant participant) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow();

        boolean allowed = conversation.getParticipants()
                .contains(participant);

        if (!allowed) {
            throw new RuntimeException("Access denied");
        }

        return conversation;
    }
}
