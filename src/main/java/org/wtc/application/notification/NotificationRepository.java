package org.wtc.application.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.notification.entity.Notification;
import org.wtc.application.participant.Participant;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByConversationIdAndReceiverAndIsReadFalse(Long conversationId, Participant receiver);
}
