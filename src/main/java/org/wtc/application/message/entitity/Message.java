package org.wtc.application.message.entitity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.message.enums.MessageStatus;

import org.wtc.application.message.enums.MessageType;
import org.wtc.application.participant.Participant;

import java.time.LocalDateTime;

@Entity
@Table(name = "wtc_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;



    @ManyToOne
    private Participant sender;

    @ManyToOne
    private Participant receiver;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime sentAt ;

    @Column(name = "is_read", nullable = false)
    private Boolean read = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;


    @Column(nullable = false)
    private Boolean deleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public static Message createMessage(
            Conversation conversation,
            String content,
            Participant sender,
            Participant receiver,
            MessageType chat) {
        Message msg = new Message();

        msg.setConversation(conversation);
        msg.setContent(content);

        msg.setSender(sender);
        msg.setReceiver(receiver);

        msg.setStatus(MessageStatus.SENT);
        msg.setRead(false);
        msg.setDeleted(false);
        msg.setType(chat);

        return msg;
    }

    public static Message createFirstMessage(
            Conversation conversation,
            String content,
            Participant clientParticipant,
            Participant operatorParticipant,
            MessageType messageType
    ) {
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation cannot be null");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        if (clientParticipant == null) {
            throw new IllegalArgumentException("Client participant cannot be null");
        }

        Message message = new Message();

        message.setConversation(conversation);
        message.setContent(content);

        message.setSender(clientParticipant);
        message.setReceiver(operatorParticipant);

        message.setStatus(MessageStatus.SENT);
        message.setRead(false);
        message.setDeleted(false);
        message.setType(messageType);

        return message;
    }



}