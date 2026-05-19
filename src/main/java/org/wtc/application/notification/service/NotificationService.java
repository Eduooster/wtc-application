package org.wtc.application.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.notification.NotificationRepository;
import org.wtc.application.notification.entity.Notification;
import org.wtc.application.participant.Participant;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void markMessagesAsRead(Long conversationId, Participant receiver) {

        List<Notification> unreadNotifications = notificationRepository
                .findByConversationIdAndReceiverAndIsReadFalse(conversationId,receiver );


        unreadNotifications.forEach(Notification::markAsRead);

    }
}
