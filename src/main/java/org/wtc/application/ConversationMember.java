package org.wtc.application;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.wtc.application.client.entity.Client;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.participant.Participant;
import org.wtc.application.user.entity.User;

import java.time.LocalDateTime;

public class ConversationMember {

    @ManyToOne
    Conversation conversation;

    @ManyToOne
    Participant participant;

    ConversationMemberRole role;
}
