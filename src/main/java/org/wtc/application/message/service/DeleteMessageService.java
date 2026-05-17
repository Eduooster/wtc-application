package org.wtc.application.message.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;

@Service
public class DeleteMessageService {

    public void deleteMessage(@AuthenticationPrincipal AuthenticableUser authenticableUser,Long messageId) {}
}
