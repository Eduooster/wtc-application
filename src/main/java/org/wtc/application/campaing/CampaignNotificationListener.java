package org.wtc.application.campaing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.wtc.application.campaing.dto.CampaignNotificationEvent;
import org.wtc.application.integration.fireBase.FirebaseNotificationService;

@Component
@RequiredArgsConstructor
public class CampaignNotificationListener {

    private final FirebaseNotificationService firebaseNotificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCampaignNotification(CampaignNotificationEvent event) {
        firebaseNotificationService.sendNotification(
                event.token(),
                event.title(),
                event.content()
        );
    }
}
