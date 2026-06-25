package org.wtc.application.conversation.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.participant.Participant;

@Service
@RequiredArgsConstructor
public class ValidateAccessConversationService {

    private final ConversationRepository conversationRepository;

    public void validateAccess(Long conversationId, Participant participant) {

        boolean allowed = conversationRepository
                .existsByIdAndParticipants_Id(conversationId, participant.getId());

        if (!allowed) {
            throw new AccessDeniedException("Access denied to this conversation");
        }
    }
}