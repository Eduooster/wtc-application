package org.wtc.application.conversation.service.useCases;

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
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.conversation.service.resolver.ConversationResolverService;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.enums.MessageType;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;
import org.wtc.application.segment.service.SegmentValidationService;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

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

        Participant clientParticipant =
                authenticatedUser.getParticipant();

        Set<Participant> participants = Set.of(clientParticipant);

        Conversation conversation = conversationResolverService.getOrCreateActiveConversation(
                participants,
                ConversationOrigin.CLIENT,
                ConversationStatus.WAITING_OPERATOR,
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

        User operator = userRepository.findByCredentials(authenticatedUser)
                .orElseThrow(() -> new EntityNotFoundException("Operator not found"));

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Target client not found"));

        segmentValidationService.ensureCompatibleSegments(operator, client);

        Participant operatorParticipant = operator.getParticipant();

        Participant clientParticipant = client.getParticipant();




        Set<Participant> participants = Set.of(operatorParticipant, clientParticipant);



        Conversation conversation = conversationResolverService.getOrCreateActiveConversation(
                participants,
                ConversationOrigin.OPERATOR,
                ConversationStatus.IN_PROGRESS,

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
