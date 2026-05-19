package org.wtc.application.segment.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.segment.dto.SegmentRequestDTO;
import org.wtc.application.segment.dto.SegmentResponseDTO;
import org.wtc.application.segment.service.ISegmentService;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/segments")
@RequiredArgsConstructor
@Tag(name = "Segmentos", description = "Endpoints para gerenciamento, criação e filtro de públicos-alvo segmentados.")
public class SegmentController {

    private final ISegmentService segmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Criar um novo segmento", description = "Registra um novo agrupamento ou segmento de clientes no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Segmento criado com sucesso.",
                    content = @Content(schema = @Schema(implementation = SegmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer papel OPERATOR ou ADMIN.", content = @Content)
    })
    public ResponseEntity<SegmentResponseDTO> createSegment(@Valid @RequestBody SegmentRequestDTO request ,@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user)
    {
        SegmentResponseDTO response = segmentService.createSegment(request,user.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Buscar segmento por ID", description = "Retorna as informações completas de um segmento específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Segmento encontrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = SegmentResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Segmento não encontrado.", content = @Content)
    })
    public ResponseEntity<SegmentResponseDTO> getSegmentById(@PathVariable @Parameter(description = "ID único do segmento", example = "1") Long id,@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user ) {
        return ResponseEntity.ok(segmentService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Listar segmentos paginados", description = "Recupera uma lista paginada com todos os segmentos configurados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de segmentos retornada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    public ResponseEntity<Page<SegmentResponseDTO>> getAllSegments(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user, Pageable pageable) {
        return ResponseEntity.ok(segmentService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Atualizar um segmento existente", description = "Modifica as propriedades ou critérios de um segmento específico com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Segmento atualizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = SegmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos são inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Segmento não encontrado.", content = @Content)
    })
    public ResponseEntity<SegmentResponseDTO> updateSegment(
            @PathVariable @Parameter(description = "ID do segmento a ser atualizado", example = "1") Long id,
            @Valid @RequestBody SegmentRequestDTO request,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(segmentService.updateSegment(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Excluir um segmento", description = "Remove logicamente ou fisicamente um segmento do sistema baseado no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Segmento removido com sucesso.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Segmento não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> deleteSegment(@PathVariable @Parameter(description = "ID do segmento a ser excluído", example = "1") Long id,
                                              @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {


        segmentService.deleteSegment(id,user.getId());
        return ResponseEntity.noContent().build();
    }
}