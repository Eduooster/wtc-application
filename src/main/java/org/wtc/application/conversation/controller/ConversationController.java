package org.wtc.application.conversation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.conversation.dto.ClientConversationRequestDTO;
import org.wtc.application.conversation.dto.ConversationResponseDto;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.entity.UserConversationRequestDTO;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.service.useCases.*;
import org.wtc.application.message.dto.MessageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.wtc.application.message.dto.SendMessageRequestDTO;
import org.wtc.application.message.service.SendMessageService;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@Tag(name = "Conversas & Atendimentos", description = "Endpoints para criação de chats, distribuição de atendimentos para operadores, entrada em conversas e listagem de mensagens.")
public class ConversationController {

    private final CreateConversationService createConversationService;
    private final FindClientConversationsService findClientConversationsService;
    private final FindAllConversationsParticipantServices findallConversationsParticipantServices;
    private final GetConversationMessagesService getConversationMessagesService;
    private final AssignedConversationService assignedConversationService;
    private final JoinConversation joinConversation;
    private final FindAllOpenConversations findAllOpenConversations;
    private final SendMessageService sendMessageService;

    @PostMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Criar conversa iniciada por um Cliente", description = "Abre um novo canal de atendimento sob a perspectiva do cliente autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conversa criada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parâmetros fornecidos inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas o perfil CLIENT pode executar essa ação.", content = @Content)
    })
    public ResponseEntity<Void> createByClient(
            @RequestBody @Valid ClientConversationRequestDTO dto,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser principal) {

        createConversationService.createByClient(dto, principal);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @Operation(summary = "Criar conversa iniciada por um Operador/Admin", description = "Abre uma nova conversa partindo da equipe interna para um cliente ativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conversa criada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Payload malformado ou inconsistente.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer papel OPERATOR ou ADMIN.", content = @Content)
    })
    public ResponseEntity<Void> createByOperator(
            @RequestBody @Valid UserConversationRequestDTO dto,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser principal) {

        createConversationService.createByOperator(dto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Filtrar conversas globais por status", description = "Recupera uma página contendo todas as conversas do sistema filtradas pelo estado solicitado (ex: OPEN, CLOSED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de conversas carregada com sucesso.", content = @Content)
    })
    public ResponseEntity<Page<ConversationResponseDto>> findAllConversationsByStatus(
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser principal,
            @RequestParam @Parameter(description = "Estado atual da conversa para filtro", example = "OPEN") ConversationStatus status,
            Pageable pageable) {

        Page<ConversationResponseDto> conversations =
                findAllOpenConversations.findAllConversationsByStatus(status, pageable);

        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/my-conversations")
    @PreAuthorize("hasAnyRole('CLIENT','OPERATOR')")
    @Operation(summary = "Listar as conversas do usuário conectado", description = "Filtra e exibe de maneira paginada apenas os chats nos quais o participante logado faz parte.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de chats do participante retornada com sucesso.", content = @Content)
    })
    public ResponseEntity<Page<ConversationResponseDto>> findByClient(
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser principal,
            Pageable pageable) {
        return ResponseEntity.ok(findallConversationsParticipantServices.findByParticipant(pageable,principal));
    }

    @GetMapping("/{id}/messages")
    @PreAuthorize("hasAnyRole('CLIENT', 'OPERATOR', 'ADMIN')")
    @Operation(summary = "Obter histórico de mensagens da conversa", description = "Busca de forma paginada o fluxo completo de mensagens trocadas em um chat específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagens da conversa retornadas com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Conversa não localizada para o ID fornecido.", content = @Content)
    })
    public ResponseEntity<Page<MessageResponseDTO>> getMessages(
            @PathVariable @Parameter(description = "ID único da conversa", example = "1") Long id,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser principal,
            Pageable pageable) {
        return ResponseEntity.ok(getConversationMessagesService.getMessagesByConversation(id, principal, pageable));
    }

    @PreAuthorize("hasRole('OPERATOR')")
    @PostMapping("/{conversationId}/assign")
    @Operation(summary = "Atribuir conversa a mim", description = "Define o operador autenticado como o responsável técnico exclusivo pela condução daquela conversa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversa vinculada ao operador com sucesso.",
                    content = @Content(schema = @Schema(implementation = ConversationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada.", content = @Content)
    })
    public ResponseEntity<ConversationResponseDto> assignConversation(
            @PathVariable @Parameter(description = "ID da conversa a receber o operador", example = "1") Long conversationId,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authenticatedUser
    ) {

        Conversation conversation = assignedConversationService.assignOperator(
                conversationId,
                authenticatedUser
        );

        return ResponseEntity.ok(new ConversationResponseDto(conversation));
    }

    @PostMapping("/{conversationId}/join")
    @PreAuthorize("hasRole('OPERATOR')")
    @Operation(summary = "Entrar em uma conversa ativa", description = "Permite que o operador entre para interagir ou acompanhar o andamento de uma conversa existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrada na conversa realizada com sucesso (sem corpo de resposta).", content = @Content),
            @ApiResponse(responseCode = "404", description = "Conversa não localizada.", content = @Content)
    })
    public ResponseEntity<ConversationResponseDto> joinConversation(
            @PathVariable @Parameter(description = "ID da conversa para ingressar", example = "1") Long conversationId,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authUser
    ) {

        joinConversation.joinConversation(
                conversationId,
                authUser
        );

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','CLIENT')")
    @PostMapping("/{conversationId}/messages")
    @Operation(summary = "Enviar uma nova mensagem", description = "Envia uma mensagem de texto ou mídia para dentro de uma conversa ativa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensagem enviada e registrada com sucesso.",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição incorretos ou conversa inválida.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado.", content = @Content)
    })

    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody SendMessageRequestDTO requestDTO, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authenticableUser, @PathVariable @Parameter(description = "ID da conversa para ingressar", example = "1") Long conversationId) {
        MessageResponseDTO response = sendMessageService.sendMessage(requestDTO,authenticableUser,conversationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}