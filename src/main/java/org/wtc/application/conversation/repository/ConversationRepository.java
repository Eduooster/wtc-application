package org.wtc.application.conversation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.participant.Participant;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c " +
            "JOIN c.participants p " +
            "WHERE p IN :participants " +
            "AND c.status = :status " +
            "GROUP BY c.id " +
            "HAVING COUNT(p) = :participantsSize")
    Optional<Conversation> findActiveConversationByParticipants(
            @Param("participants") Set<Participant> participants,
            @Param("participantsSize") Long participantsSize,
            @Param("status") ConversationStatus status
    );

    Page<Conversation> findByParticipantsContains(Participant participant, Pageable pageable);

    Optional<Conversation> findFirstByParticipantsContainsAndStatus(Set<Participant> participant, ConversationStatus conversationStatus);

    Page<Conversation> findByParticipants_IdAndDeletedFalse(Long id, Pageable pageable);

    Optional<Conversation> findActiveByParticipants(Participant clientParticipant);

    boolean existsByIdAndParticipants_Id(Long conversationId, Long id);

    Page<Conversation> findAllByStatus(ConversationStatus status,Pageable pageable);
}
