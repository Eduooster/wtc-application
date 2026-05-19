package org.wtc.application.conversation.service;



import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.auth.entity.RoleProfile;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.message.MessageMapper;
import org.wtc.application.message.dto.MessageResponseDTO;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;


@Service
@RequiredArgsConstructor
public class GetConversationMessagesService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ParticipantRepository participantRepository;


    @Transactional(readOnly = true)
    public Page<MessageResponseDTO> getMessagesByConversation(
            Long conversationId,
            AuthenticableUser principal,
            Pageable pageable
    ) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));

        Participant participant = participantRepository
                .findById(principal.getParticipant().getId())
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        boolean belongsToConversation = conversation.getParticipants()
                .contains(participant);

        if (!belongsToConversation) {
            throw new AccessDeniedException(
                    "You do not have permission to view this conversation."
            );
        }

        return messageRepository.findByConversation(conversation, pageable)
                .map(MessageResponseDTO::new);
    }
}