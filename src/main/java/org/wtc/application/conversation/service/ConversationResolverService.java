package org.wtc.application.conversation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.conversation.dto.ConversationCreationContext;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.enums.ConversationOrigin;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.enums.ConversationTypeOrigin;
import org.wtc.application.conversation.factory.ConversationFactory;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.participant.Participant;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConversationResolverService {

    private final ConversationRepository conversationRepository;
    private final ConversationFactory conversationFactory;

    public Conversation getOrCreateActiveConversation(
            Set<Participant> participants,
            ConversationOrigin origin,
            ConversationStatus statusForCreation,
            ConversationTypeOrigin typeOrigin,
            String title,
            Participant assignedOperator
    ) {


        return conversationRepository
                .findActiveConversationByParticipants(participants, (long) participants.size(), ConversationStatus.IN_PROGRESS)
                .orElseGet(() -> {

                    Conversation newConversation = Conversation.create(
                            new ConversationCreationContext(
                                    participants,
                                    origin,
                                    typeOrigin,
                                    statusForCreation,
                                    title,
                                    assignedOperator
                            )
                    );

                    return conversationRepository.save(newConversation);
                });
    }
}