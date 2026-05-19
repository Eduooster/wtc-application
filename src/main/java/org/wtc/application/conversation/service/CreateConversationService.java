package org.wtc.application.conversation.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.entity.UserConversationRequestDTO;
import org.wtc.application.conversation.dto.ClientConversationRequestDTO;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.enums.ConversationOrigin;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.enums.ConversationTypeOrigin;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.enums.MessageType;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.service.SegmentValidationService;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    private final ClientRepository clientRepository;

    private final ParticipantRepository participantRepository;
    private final ConversationResolverService conversationResolverService;
    private final SegmentValidationService segmentValidationService;
    private final UserRepository userRepository;

    @Transactional
    public Conversation createByClient(
            ClientConversationRequestDTO request,
            AuthenticableUser authenticatedUser
    ) {



        Participant clientParticipant = participantRepository.findByParticipantTypeAndRefId(ParticipantType.CLIENT,authenticatedUser.getParticipant().getRefId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Set<Participant> participants = Set.of(clientParticipant);

        Conversation conversation = conversationResolverService.getOrCreateActiveConversation(
                participants,
                ConversationOrigin.CLIENT,
                ConversationStatus.WAITING_OPERATOR,
                ConversationTypeOrigin.CHAT,
                request.title(),
                null
        );



        Message firstMessage = Message.createFirstMessage(
                conversation,
                request.firstMessage(),
                clientParticipant,
                null,
                MessageType.CHAT
        );

        conversation.updateLastMessage();

        conversationRepository.save(conversation);
        messageRepository.save(firstMessage);

        return conversation;
    }
    @Transactional
    public Conversation createByOperator(UserConversationRequestDTO request, AuthenticableUser authenticatedUser) {

        Participant operatorParticipant = participantRepository
                .findByParticipantTypeAndRefId(ParticipantType.OPERATOR, authenticatedUser.getParticipant().getRefId())
                .orElseThrow(() -> new RuntimeException("Operator participant not found"));

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new RuntimeException("Target client not found"));

        Participant clientParticipant = participantRepository
                .findByParticipantTypeAndRefId(ParticipantType.CLIENT, client.getId())
                .orElseThrow(() -> new RuntimeException("Client participant not found"));



        segmentValidationService.validateByIds(request.clientId(), authenticatedUser.getParticipant().getRefId());

        Set<Participant> participants = Set.of(operatorParticipant, clientParticipant);



        Conversation conversation = conversationResolverService.getOrCreateActiveConversation(
                participants,
                ConversationOrigin.OPERATOR,
                ConversationStatus.IN_PROGRESS,
                ConversationTypeOrigin.CHAT,
                request.title(),
                operatorParticipant


        );


        Message firstMessage = Message.createFirstMessage(
                conversation,
                request.firstMessage(),
                operatorParticipant,
                clientParticipant,
                MessageType.CHAT
        );


        conversationRepository.save(conversation);
        messageRepository.save(firstMessage);

        return conversation;
    }



}
