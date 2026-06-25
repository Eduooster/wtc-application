package org.wtc.application.conversation.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateOperatorCanJoin {

//    public void validateOperatorCanJoin(Conversation conversation, Participant operator) {
//
//        if (conversation.getOperators().contains(operator)) {
//            throw new RuntimeException("Operator already in conversation");
//        }
//
//        boolean hasCompatibleSegment =
//                operator.getSegments().stream()
//                        .anyMatch(seg -> conversation.getClient().getSegments().contains(seg));
//
//        if (!hasCompatibleSegment) {
//            throw new RuntimeException("Operator not allowed to join this conversation");
//        }
//    }

}
