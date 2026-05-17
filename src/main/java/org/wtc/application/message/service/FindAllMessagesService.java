package org.wtc.application.message.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.MessageMapper;
import org.wtc.application.message.dto.MessageResponseDTO;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;

@Service
@RequiredArgsConstructor
public class FindAllMessagesService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public Page<MessageResponseDTO> findAllMessages(
            Pageable pageable,
            AuthenticableUser authenticableUser
    ) {

        Participant participant = participantRepository
                .findById(authenticableUser.getParticipant().getId())
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        return messageRepository
                .findBySenderOrReceiver(participant, participant, pageable)
                .map(messageMapper::toResponseDto);
    }
}
