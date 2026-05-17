package org.wtc.application.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.dto.MessageResponseDTO;
import org.wtc.application.message.dto.SendMessageRequestDTO;
import org.wtc.application.message.service.DeleteMessageService;
import org.wtc.application.message.service.FindAllMessagesService;
import org.wtc.application.message.service.MarkMessageAsReadService;

import org.wtc.application.message.service.SendMessageService;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final SendMessageService sendMessageService;
    private final FindAllMessagesService findAllMessagesService;
    private final DeleteMessageService deleteMessage;
    private final MarkMessageAsReadService markMessageAsReadService;


    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody SendMessageRequestDTO requestDTO, @AuthenticationPrincipal AuthenticableUser authenticableUser) {
        MessageResponseDTO response = sendMessageService.sendMessage(requestDTO,authenticableUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<MessageResponseDTO>> listMessages(Pageable pageable, @AuthenticationPrincipal AuthenticableUser authenticableUser) {
        Page<MessageResponseDTO> messages = findAllMessagesService.findAllMessages(pageable,authenticableUser);
        return ResponseEntity.ok(messages);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> readMessage(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser authenticableUser ) {
        markMessageAsReadService.markMessageAsRead(id,authenticableUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser authenticableUser) {
        deleteMessage.deleteMessage(authenticableUser,id);
        return ResponseEntity.noContent().build();
    }
}
