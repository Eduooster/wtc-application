package org.wtc.application.conversation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.participant.Participant;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {



    Page<Conversation> findByParticipantsContains(Participant participant, Pageable pageable);

    Optional<Conversation> findFirstByParticipantsContainsAndStatus(Participant participant, ConversationStatus conversationStatus);

    Page<Conversation> findByParticipants_IdAndDeletedFalse(Long id, Pageable pageable);
}
