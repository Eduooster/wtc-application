package org.wtc.application.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.notification.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PatchMapping("/{conversationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal AuthenticableUser user) {

        notificationService.markMessagesAsRead(conversationId, user.getParticipant());
        return ResponseEntity.noContent().build();
    }
}
