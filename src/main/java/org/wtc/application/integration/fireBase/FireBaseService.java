package org.wtc.application.integration.fireBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wtc.application.client.entity.Client;

@Service
@Slf4j
public class FireBaseService {


    public void sendPush(Client client, String title, String content) {
        log.info("fire base send push");
    }
}
