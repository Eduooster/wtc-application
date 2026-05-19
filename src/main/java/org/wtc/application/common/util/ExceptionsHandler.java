package org.wtc.application.common.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wtc.application.campaing.exceptions.CampaignNotFoundException;
import org.wtc.application.conversation.exceptions.ConversationAlreadyAssignedException;
import org.wtc.application.conversation.exceptions.InvalidConversationStatusException;
import org.wtc.application.message.exceptions.ChatAccessDeniedException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {

    public ExceptionsHandler() {}

   @ExceptionHandler(CampaignNotFoundException.class)
    public ResponseEntity<Map<String, String>> ResourceNotFound(CampaignNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.detalhar(ex));
    }

    @ExceptionHandler(ChatAccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleChatAccessDenied(ChatAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(this.detalhar(ex));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.detalhar(ex));
    }

    @ExceptionHandler(ConversationAlreadyAssignedException.class)
    public ResponseEntity<Map<String, String>> handleConversationAlreadyAssignedException(ConversationAlreadyAssignedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.detalhar(ex));
    }

    @ExceptionHandler(InvalidConversationStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidConversationStatusException(InvalidConversationStatusException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.detalhar(ex));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(detalhar(ex));
    }



    public Map<String, String> detalhar(Exception ex) {
        Map<String, String> map = new HashMap();
        map.put("erro", ex.getClass().getSimpleName());
        map.put("mensagem", ex.getMessage());
        return map;
    }
}
