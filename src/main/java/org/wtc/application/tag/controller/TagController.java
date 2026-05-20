package org.wtc.application.tag.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.tag.dto.TagRequestDTO;
import org.wtc.application.tag.dto.TagResponseDTO;
import org.wtc.application.tag.service.ITagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@Tag(name = "Etiquetas (Tags)", description = "Endpoints para criação, edição, listagem e exclusão de tags de classificação de clientes.")
public class TagController {

    private final ITagService tagService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Criar uma nova tag", description = "Cadastra uma nova etiqueta identificadora no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag criada com sucesso.",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou dados malformados.", content = @Content)
    })
    public ResponseEntity<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        TagResponseDTO response = tagService.createTag(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Buscar tag por ID", description = "Retorna os detalhes de uma etiqueta específica através do ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag localizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada.", content = @Content)
    })
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable @Parameter(description = "ID exclusivo da tag", example = "1") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Listar todas as tags", description = "Retorna uma lista completa de todas as etiquetas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tags recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagResponseDTO.class))))
    })
    public ResponseEntity<List<TagResponseDTO>> getAllTags(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(tagService.findAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Atualizar uma tag existente", description = "Modifica as propriedades de uma etiqueta baseando-se no ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag atualizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada.", content = @Content)
    })
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable @Parameter(description = "ID da tag a ser editada", example = "1") Long id,
            @Valid @RequestBody TagRequestDTO request,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Remover uma tag", description = "Exclui definitivamente uma tag do sistema por meio do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deletada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada.", content = @Content)
    })
    public ResponseEntity<Void> deleteTag(@PathVariable @Parameter(description = "ID da tag a ser removida", example = "1") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}