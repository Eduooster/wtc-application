package org.wtc.application.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.client.entity.Client;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.notification.enums.NotificationStatus;
import org.wtc.application.participant.Participant;

import java.time.LocalDateTime;

@Entity
@Table(name = "wtc_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Conversation conversation;

    @ManyToOne
    private Participant receiver;

    private String previewContent;

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void markAsRead() {
        this.isRead = true;
    }
}