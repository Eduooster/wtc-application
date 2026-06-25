package org.wtc.application.conversation.service.useCases;


import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.conversation.dto.ConversationResponseDto;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.mapper.ConversationMapper;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;

@Service
@RequiredArgsConstructor
public class FindAllConversationsParticipantServices {

    private final ConversationRepository conversationRepository;


    private final ConversationMapper conversationMapper;
    private final ParticipantRepository participantRepository;



    @Transactional(readOnly = true)
    public Page<ConversationResponseDto> findByParticipant(Pageable pageable, AuthenticableUser authenticableUser) {

        Participant participant = participantRepository.findById(
                authenticableUser.getId()).orElseThrow(() -> new RuntimeException("Participant not found"));


        Page<Conversation> conversations =
                conversationRepository.findByParticipants_IdAndDeletedFalse(
                        participant.getId(),
                        pageable
                );

        return conversations.map(conversationMapper::toResponseDto);

    }
}
