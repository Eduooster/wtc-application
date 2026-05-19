package org.wtc.application.audit.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.audit.dto.AuditRequestDTO;
import org.wtc.application.audit.dto.AuditResponseDTO;
import org.wtc.application.audit.service.IAuditService;
import org.wtc.application.auth.entity.AuthenticableUser;

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
@RequestMapping("/audits")
@RequiredArgsConstructor
@Tag(name = "Auditoria", description = "Endpoints para registro, consulta e rastreamento de logs e ações de auditoria no sistema.")
public class AuditController {

    private final IAuditService auditService;

    @PostMapping
    @Operation(summary = "Criar um novo registro de auditoria", description = "Registra uma nova ocorrência ou log de auditoria de forma manual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro de auditoria criado com sucesso.",
                    content = @Content(schema = @Schema(implementation = AuditResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos na requisição são inválidos.", content = @Content)
    })
    public ResponseEntity<AuditResponseDTO> createAudit(@Valid @RequestBody AuditRequestDTO request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        AuditResponseDTO response = auditService.createAudit(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro de auditoria por ID", description = "Retorna os detalhes de um log de auditoria específico através do identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de auditoria localizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = AuditResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Registro de auditoria não encontrado.", content = @Content)
    })
    public ResponseEntity<AuditResponseDTO> getAuditById(@PathVariable @Parameter(description = "ID exclusivo do registro de auditoria", example = "1") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(auditService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os registros de auditoria", description = "Retorna uma lista completa contendo todos os logs de auditoria armazenados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de auditorias recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuditResponseDTO.class))))
    })
    public ResponseEntity<List<AuditResponseDTO>> getAllAudits() {
        return ResponseEntity.ok(auditService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um registro de auditoria existente", description = "Modifica as informações de um log de auditoria com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de auditoria atualizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = AuditResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro de auditoria não encontrado.", content = @Content)
    })
    public ResponseEntity<AuditResponseDTO> updateAudit(
            @PathVariable @Parameter(description = "ID do registro de auditoria a ser editado", example = "1") Long id,
            @Valid @RequestBody AuditRequestDTO request) {
        return ResponseEntity.ok(auditService.updateAudit(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um registro de auditoria", description = "Exclui permanentemente um log de auditoria do sistema por meio do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro de auditoria deletado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro de auditoria não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> deleteAudit(@PathVariable @Parameter(description = "ID do registro de auditoria a ser removido", example = "1") Long id) {
        auditService.deleteAudit(id);
        return ResponseEntity.noContent().build();
    }
}