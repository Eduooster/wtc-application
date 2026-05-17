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
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    private final ClientRepository clientRepository;

    private final ParticipantRepository participantRepository;

    @Transactional
    public Conversation createByClient(
            ClientConversationRequestDTO request,
            AuthenticableUser authenticatedUser
    ) {



        Participant client = participantRepository.findByParticipantTypeAndRefId(ParticipantType.CLIENT,authenticatedUser.getParticipant().getRefId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Conversation conversation = getOrCreateActiveConversation(client);

        Conversation.applyClientContext(
                conversation,
                client,
                request.title()
        );


        Message firstMessage = Message.createFirstMessage(
                conversation,
                request.firstMessage(),
                client,
                null
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


        Conversation conversation = getOrCreateActiveConversation(clientParticipant);


        Conversation.applyOperatorContext(
                conversation,
                operatorParticipant,
                request.title()
        );


        Message firstMessage = Message.createFirstMessage(
                conversation,
                request.firstMessage(),
                operatorParticipant,
                clientParticipant
        );


        conversationRepository.save(conversation);
        messageRepository.save(firstMessage);

        return conversation;
    }

    private Conversation getOrCreateActiveConversation(Participant participant) {
        return conversationRepository
                .findFirstByParticipantsContainsAndStatus(participant, ConversationStatus.IN_PROGRESS)
                .orElseGet(() -> createNewConversationInstance(participant));
    }


    private Conversation createNewConversationInstance(Participant participant) {
        Conversation conversation = new Conversation();
        conversation.getParticipants().add(participant);
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setDeleted(false);

        return conversation;
    }

}
