package org.wtc.application.conversation.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.conversation.dto.ConversationCreationContext;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.repository.ConversationRepository;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class ConversationFactory {



    public Conversation create(ConversationCreationContext ctx) {

        Conversation conversation = new Conversation();

        conversation.setTitle(ctx.title());
        conversation.setParticipants(new HashSet<>(ctx.participants()));
        conversation.setStatus(ctx.status());
        conversation.setOrigin(ctx.origin());
        conversation.setConversationTypeOrigin(ctx.typeOrigin());

        conversation.setActive(true);
        conversation.setDeleted(false);
        conversation.setLastMessageAt(LocalDateTime.now());
        conversation.setAssignedOperator(ctx.assignedOperator());
        conversation.setLastMessageAt(LocalDateTime.now());

        return conversation;
    }
}