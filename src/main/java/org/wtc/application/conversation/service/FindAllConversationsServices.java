package org.wtc.application.conversation.service;


import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.dto.ConversationResponseDto;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.mapper.ConversationMapper;
import org.wtc.application.conversation.repository.ConversationRepository;

@Service
@RequiredArgsConstructor
public class FindAllConversationsServices {

    private final ConversationRepository conversationRepository;


    private final ConversationMapper conversationMapper;
    private final ClientRepository clientRepository;


    @Transactional(readOnly = true)
    public Page<ConversationResponseDto> findByClient(Pageable pageable, AuthenticableUser authenticableUser) {
        Client client = clientRepository
                .findByCredentials(authenticableUser)
                .orElseThrow(() -> new RuntimeException("Client not found"));


        Page<Conversation> conversations =
                conversationRepository.findByParticipants_IdAndDeletedFalse(
                        client.getId(),
                        pageable
                );

        return conversations.map(conversationMapper::toResponseDto);



    }
}
