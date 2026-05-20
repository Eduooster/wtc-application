package org.wtc.application.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.dto.MessageResponseDTO;
import org.wtc.application.message.dto.SendMessageRequestDTO;
import org.wtc.application.message.service.DeleteMessageService;
import org.wtc.application.message.service.MarkMessageAsReadService;

import org.wtc.application.message.service.SendMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Mensagens", description = "Endpoints para envio, leitura e exclusão de mensagens dentro dos canais de conversa.")
public class MessageController {

    private final SendMessageService sendMessageService;
    private final DeleteMessageService deleteMessage;
    private final MarkMessageAsReadService markMessageAsReadService;

    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','CLIENT')")
    @PostMapping
    @Operation(summary = "Enviar uma nova mensagem", description = "Envia uma mensagem de texto ou mídia para dentro de uma conversa ativa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensagem enviada e registrada com sucesso.",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição incorretos ou conversa inválida.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado.", content = @Content)
    })
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody SendMessageRequestDTO requestDTO, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authenticableUser) {
        MessageResponseDTO response = sendMessageService.sendMessage(requestDTO,authenticableUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','CLIENT')")
    @PatchMapping("/{id}/read")
    @Operation(summary = "Marcar mensagem como lida", description = "Atualiza o status de leitura de uma mensagem específica para o usuário conectado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mensagem marcada como lida com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Mensagem não encontrada.", content = @Content)
    })
    public ResponseEntity<Void> readMessage(@PathVariable @Parameter(description = "ID único da mensagem", example = "105") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authenticableUser ) {
        markMessageAsReadService.markMessageAsRead(id,authenticableUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','CLIENT')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma mensagem", description = "Remove ou revoga uma mensagem enviada com base no ID fornecido e nas permissões do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mensagem excluída com sucesso.", content = @Content),
            @ApiResponse(responseCode = "403", description = "O usuário não tem permissão para deletar esta mensagem.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Mensagem não encontrada.", content = @Content)
    })
    public ResponseEntity<Void> deleteMessage(@PathVariable @Parameter(description = "ID único da mensagem a ser deletada", example = "105") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authenticableUser) {
        deleteMessage.deleteMessage(authenticableUser,id);
        return ResponseEntity.noContent().build();
    }
}