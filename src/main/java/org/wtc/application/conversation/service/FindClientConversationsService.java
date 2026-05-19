package org.wtc.application.conversation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.repository.ClientRepository;

import org.wtc.application.conversation.dto.ConversationResponseDto;
import org.wtc.application.conversation.mapper.ConversationMapper;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;
import org.wtc.application.participant.ParticipantService;
import org.wtc.application.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FindClientConversationsService {

    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantService participantService;

    @Transactional(readOnly = true)
    public Page<ConversationResponseDto> findAll(Pageable pageable, AuthenticableUser principal) {

        Participant participant = participantService.resolve(principal);


        return conversationRepository
                .findByParticipantsContains(participant, pageable)
                .map(conversationMapper::toResponseDto);
    }
}
