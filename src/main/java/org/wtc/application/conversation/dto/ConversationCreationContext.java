package org.wtc.application.conversation.dto;

import org.wtc.application.conversation.enums.ConversationOrigin;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.enums.ConversationTypeOrigin;
import org.wtc.application.participant.Participant;

import java.util.Set;

public record ConversationCreationContext(
        Set<Participant> participants,
        ConversationOrigin origin,
        ConversationTypeOrigin typeOrigin,
        ConversationStatus status,
        String title,
        Participant assignedOperator
) {}
