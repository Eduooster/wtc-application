package org.wtc.application.message.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.conversation.service.validation.ValidateAccessConversationService;
import org.wtc.application.message.dto.MessageResponseDTO;
import org.wtc.application.message.dto.SendMessageRequestDTO;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.enums.MessageType;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.notification.NotificationRepository;
import org.wtc.application.notification.entity.Notification;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;

@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ValidateAccessConversationService validateAccessConversationService;


    private final ParticipantRepository participantRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationRepository notificationRepository;

    @Transactional
    public MessageResponseDTO sendMessage(
            @Valid SendMessageRequestDTO request,
            AuthenticableUser authenticableUser,
            Long conversationId) {


        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));



        Participant sender =
                authenticableUser.getParticipant();




        boolean isAllowed = conversation.getParticipants().contains(sender) ||
                conversation.getParticipants().contains(sender);

        if (!isAllowed) {
            throw new AccessDeniedException("Você não pertence a esta conversa e não pode enviar mensagens nela.");
        }





        Message message = Message.createMessage(
                conversation,
                request.content(),
                sender,
                MessageType.CHAT
        );

        conversation.updateLastMessage();
        Message savedMessage = messageRepository.save(message);

        Notification notification = new Notification();
        notification.setConversation(conversation);
        notification.setPreviewContent(request.content());
        notification.setRead(false);

        notificationRepository.save(notification);

        return new MessageResponseDTO(savedMessage);
    }
}