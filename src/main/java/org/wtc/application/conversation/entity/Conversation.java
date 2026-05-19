package org.wtc.application.conversation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.wtc.application.conversation.dto.ConversationCreationContext;
import org.wtc.application.conversation.enums.ConversationOrigin;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.enums.ConversationTypeOrigin;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.participant.Participant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "wtc_conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    private String title;

    @ManyToMany
    @JoinTable(
            name = "conversation_participants",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private Set<Participant> participants = new HashSet<>();



    private LocalDateTime lastMessageAt;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_operator_id")
    private Participant assignedOperator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationOrigin origin;

    @Enumerated(EnumType.STRING)
    private ConversationTypeOrigin conversationTypeOrigin;


    public void updateLastMessage() {
        this.lastMessageAt = LocalDateTime.now();
    }


    public static Conversation create(ConversationCreationContext ctx) {

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
