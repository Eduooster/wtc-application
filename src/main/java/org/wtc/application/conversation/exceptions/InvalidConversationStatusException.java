package org.wtc.application.conversation.exceptions;

public class InvalidConversationStatusException extends RuntimeException {
    public InvalidConversationStatusException(String message) {
        super(message);
    }
}
