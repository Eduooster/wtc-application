package org.wtc.application.conversation.exceptions;

public class ConversationAlreadyAssignedException extends RuntimeException {
    public ConversationAlreadyAssignedException(String message) {
        super(message);
    }
}
