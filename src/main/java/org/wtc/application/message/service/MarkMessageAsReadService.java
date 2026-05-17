package org.wtc.application.message.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MarkMessageAsReadService {


    private final MessageRepository messageRepository;

    @Transactional
    public void markMessageAsRead(Long messageId, AuthenticableUser authenticableUser) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));


        Long currentUserId = authenticableUser.getParticipant().getId();
        var currentUserType = authenticableUser.getParticipant().getParticipantType();


        var receiver = message.getReceiver();


        boolean isReceiver = receiver.getId().equals(currentUserId)
                && receiver.getParticipantType().equals(currentUserType);

        if (!isReceiver) {
            throw new AccessDeniedException("User is not authorized to read this message");
        }

        message.setRead(true);

    }
}
