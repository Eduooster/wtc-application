package org.wtc.application.conversation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.dto.ClientConversationRequestDTO;
import org.wtc.application.conversation.dto.ConversationResponseDto;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.entity.UserConversationRequestDTO;
import org.wtc.application.conversation.service.*;
import org.wtc.application.message.dto.MessageResponseDTO;


@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final CreateConversationService createConversationService;
    private final FindClientConversationsService findClientConversationsService;
    private final FindAllConversationsServices findAllConversationsServices;
    private final GetConversationMessagesService getConversationMessagesService;
    private final AssignedConversationService assignedConversationService;


    @PostMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> createByClient(
            @RequestBody @Valid ClientConversationRequestDTO dto,
            @AuthenticationPrincipal AuthenticableUser principal) {

        createConversationService.createByClient(dto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/user")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    public ResponseEntity<Void> createByOperator(
            @RequestBody @Valid UserConversationRequestDTO dto,
            @AuthenticationPrincipal AuthenticableUser principal) {

        createConversationService.createByOperator(dto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    public ResponseEntity<Page<ConversationResponseDto>> findAll(Pageable pageable, @AuthenticationPrincipal AuthenticableUser principal) {
        return ResponseEntity.ok(findClientConversationsService.findAll(pageable,principal));
    }

    @GetMapping("/my-conversations")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<ConversationResponseDto>> findByClient(
            @AuthenticationPrincipal AuthenticableUser principal,
            Pageable pageable) {
        return ResponseEntity.ok(findAllConversationsServices.findByClient(pageable,principal));
    }

    @GetMapping("/{id}/messages")
    @PreAuthorize("hasAnyRole('CLIENT', 'OPERATOR', 'ADMIN')")
    public ResponseEntity<Page<MessageResponseDTO>> getMessages(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticableUser principal,
            Pageable pageable) {
        return ResponseEntity.ok(getConversationMessagesService.getMessagesByConversation(id, principal, pageable));
    }

    @PostMapping("/{conversationId}/assign")
    public ResponseEntity<ConversationResponseDto> assignConversation(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal AuthenticableUser authenticatedUser
    ) {

        Conversation conversation = assignedConversationService.assignOperator(
                conversationId,
                authenticatedUser
        );

        return ResponseEntity.ok(new ConversationResponseDto(conversation));
    }


}
