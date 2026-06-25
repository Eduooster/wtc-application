package org.wtc.application.segment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.participant.Participant;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SegmentValidationService {






    public void ensureCompatibleSegments(User operator, Client client) {
        boolean hasCompatibleSegment = operator.getSegments()
                .stream()
                .anyMatch(segment -> client.getSegments().contains(segment));

        if (!hasCompatibleSegment) {
            throw new AccessDeniedException(
                    "Operator does not have compatible segments to answer this client"
            );
        }
    }
}