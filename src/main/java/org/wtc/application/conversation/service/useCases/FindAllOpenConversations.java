package org.wtc.application.conversation.service.useCases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wtc.application.conversation.dto.ConversationResponseDto;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.mapper.ConversationMapper;
import org.wtc.application.conversation.repository.ConversationRepository;

@Service
@RequiredArgsConstructor
public class FindAllOpenConversations {

    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;

    public Page<ConversationResponseDto> findAllConversationsByStatus(
            ConversationStatus status,
            Pageable pageable) {

        return conversationRepository
                .findAllByStatus(status, pageable)
                .map(conversationMapper::toResponseDto);
    }
}
