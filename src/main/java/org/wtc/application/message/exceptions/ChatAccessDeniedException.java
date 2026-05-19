package org.wtc.application.message.exceptions;

public class ChatAccessDeniedException extends RuntimeException {
    public ChatAccessDeniedException(String message) {
        super(message);
    }
}
