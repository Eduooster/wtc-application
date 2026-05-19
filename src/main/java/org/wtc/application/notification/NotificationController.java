package org.wtc.application.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.notification.dto.NotificationResponseDTO;
import org.wtc.application.notification.entity.Notification;
import org.wtc.application.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notificações", description = "Endpoints para gerenciamento de alertas e leitura de notificações de conversas.")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PatchMapping("/{conversationId}/read")
    @Operation(summary = "Marcar notificações da conversa como lidas", description = "Zera os alertas pendentes de uma conversa específica para o participante autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificações atualizadas com sucesso (sem conteúdo).", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada.", content = @Content)
    })
    public ResponseEntity<Void> markAsRead(
            @PathVariable @Parameter(description = "ID da conversa cujas notificações serão lidas", example = "1") Long conversationId,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {

        notificationService.markMessagesAsRead(conversationId, user.getParticipant());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notifications/unread")
    @Operation(summary = "Listar notificações não lidas do usuário", description = "Retorna uma página contendo os alertas pendentes vinculados ao usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de notificações recuperada com sucesso.", content = @Content)
    })
    public ResponseEntity<Page<NotificationResponseDTO>> unreadNotifications(
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user, Pageable pageable
    ){

        return ResponseEntity.ok(notificationService.getMyNotifications(user.getId(),pageable));
    }
}