package org.wtc.application.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class DeleteMessageService {

    private final MessageRepository messageRepository;
    public void deleteMessage(@AuthenticationPrincipal AuthenticableUser authenticableUser,
                              Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));


        if (!message.getSender().getId().equals(authenticableUser.getId())) {
            throw new RuntimeException("You cannot delete this message");
        }

        message.setDeleted(true);

        messageRepository.save(message);
    }
}
