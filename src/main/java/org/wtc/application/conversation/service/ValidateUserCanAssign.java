package org.wtc.application.conversation.service;

import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.participant.Participant;

public class ValidateUserCanAssign {

//    public void validateOperatorCanAssign(Conversation conversation, Participant operator) {
//
//        if (conversation.getAssignedOperator() != null) {
//            throw new RuntimeException("Conversation already assigned");
//        }
//
//        if (conversation.getStatus() != ConversationStatus.WAITING_OPERATOR) {
//            throw new RuntimeException("Conversation not available for assignment");
//        }
//
//        boolean hasCompatibleSegment =
//                operator.getSegments().stream()
//                        .anyMatch(seg -> conversation.getClient().getSegments().contains(seg));
//
//        if (!hasCompatibleSegment) {
//            throw new RuntimeException("Operator not allowed to assign this conversation");
//        }
//    }
}
