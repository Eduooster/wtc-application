package org.wtc.application.message.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.participant.Participant;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId, Pageable pageable);

    Page<Message> findByConversation(Conversation conversation, Pageable pageable);

    Page<Message> findBySenderOrReceiver(
            Participant sender,
            Participant receiver,
            Pageable pageable
    );
}
