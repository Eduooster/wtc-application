package org.wtc.application.client.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.dto.ClientRequestDTO;
import org.wtc.application.client.dto.ClientResponseDTO;
import org.wtc.application.client.service.IClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.client.service.UpdateClientSegments;
import org.wtc.application.client.service.UpdateClientTagsService;
import org.wtc.application.client.service.UpdateFireBaseToken;
import org.wtc.application.integration.fireBase.ClientFirebaseTokenService;
import org.wtc.application.integration.fireBase.DeviceTokenDto;
import org.wtc.application.integration.fireBase.FireBaseTokenRequestDto;
import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;
import org.wtc.application.user.dto.UpdateUserTagRequestDto;
import org.wtc.application.user.service.UpdateUserSegments;


import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para cadastro, segmentação, etiquetagem (tags) e gerenciamento de tokens Firebase/Dispositivos dos clientes.")
public class ClientController {

    private final IClientService clientService;
    private final UpdateClientSegments updateSegments;
    private final ClientFirebaseTokenService clientFirebaseTokenService;
    private final UpdateFireBaseToken updateFireBaseToken;
    private final UpdateClientTagsService updateClientTagsService;

    @PostMapping
    @Operation(summary = "Cadastrar um novo cliente", description = "Registra um novo cliente no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados enviados na requisição são inválidos.", content = @Content)
    })
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO request,@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        ClientResponseDTO response = clientService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{clientId}/segments")
    @Operation(summary = "Atualizar segmentos do cliente", description = "Vincula ou desvincula o cliente a segmentos de mercado específicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Segmentos do cliente atualizados com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> updateUserSegments(
            @PathVariable @Parameter(description = "ID do cliente", example = "1") Long clientId,
            @RequestBody UpdateUserSegmentsRequestDTO request
    ) {
        updateSegments.updateClientSegments(clientId, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/tags")
    @Operation(summary = "Atualizar tags do cliente", description = "Substitui ou atualiza as etiquetas (tags) associadas ao cliente informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tags atualizadas com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> updateUserTags(
            @PathVariable @Parameter(description = "ID do cliente (mapeado como userId no serviço)", example = "1") Long userId,
            @RequestBody UpdateUserTagRequestDto request
    ) {
        updateClientTagsService.updateTagClient(request, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/firebase-token")
    @Operation(summary = "Salvar token Firebase do cliente autenticado", description = "Associa o token de notificações push do Firebase ao perfil do cliente logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Token Firebase salvo com sucesso.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado.", content = @Content)
    })
    public ResponseEntity<Void> saveFirebaseToken(
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser principal,
            @RequestBody FireBaseTokenRequestDto dto
    ) {
        clientFirebaseTokenService.saveToken(principal, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/device-token")
    @Operation(summary = "Atualizar token de dispositivo (Firebase) por ID", description = "Atualiza o token de push do dispositivo de um cliente específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Token de dispositivo atualizado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> updateDeviceToken(
            @PathVariable @Parameter(description = "ID do cliente", example = "1") Long id,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authenticableUser,
            @RequestBody @Valid DeviceTokenDto dto) {
        updateFireBaseToken.updateFirebaseToken(id,dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Busca detalhada das informações cadastrais de um cliente pelo seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente localizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable @Parameter(description = "ID único do cliente", example = "1") Long id,@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista contendo todos os clientes ativos cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClientResponseDTO.class))))
    })
    public ResponseEntity<List<ClientResponseDTO>> getAllClients(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(clientService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados cadastrais do cliente", description = "Modifica as informações gerais de um cliente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable @Parameter(description = "ID do cliente a ser atualizado", example = "1") Long id,
            @Valid @RequestBody ClientRequestDTO request,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um cliente", description = "Realiza a exclusão do cliente do sistema através do ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> deleteClient(@PathVariable @Parameter(description = "ID do cliente a ser removido", example = "1") Long id,@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user
    ) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}