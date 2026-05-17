package org.wtc.application.message.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.message.dto.MessageResponseDTO;
import org.wtc.application.message.dto.SendMessageRequestDTO;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;

@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;


    private final ParticipantRepository participantRepository;

    @Transactional
    public MessageResponseDTO sendMessage(
            @Valid SendMessageRequestDTO request,
            AuthenticableUser authenticableUser
    ) {

        Conversation conversation = conversationRepository.findById(request.conversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));


        Participant sender = participantRepository.findByParticipantTypeAndRefId(authenticableUser.getParticipant().getParticipantType(),authenticableUser.getParticipant().getRefId())
                .orElseThrow(() -> new EntityNotFoundException("Sender error"));



        Participant receiver = participantRepository
                .findByParticipantTypeAndRefId(
                        request.recipientType(),
                        request.recipientId()
                )
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));


        Message message = Message.createMessage(
                conversation,
                request.content(),
                sender,
                receiver
        );

        conversation.updateLastMessage();

        Message savedMessage = messageRepository.save(message);

        return new MessageResponseDTO(savedMessage);
    }
}