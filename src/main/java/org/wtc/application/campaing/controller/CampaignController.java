package org.wtc.application.campaing.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.campaing.dto.CampaignScheduleRequestDto;

import org.wtc.application.campaing.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Campanhas", description = "Endpoints para criação, agendamento, disparo e gerenciamento completo de campanhas de marketing.")
public class CampaignController {

    private final ICampaignService campaignService;
    private final CreateCampaign createCampaign;
    private final SendCampaign sendCampaign;
    private final ScheduleCampaign scheduleCampaign;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Criar uma nova campanha", description = "Registra uma nova campanha no sistema vinculada ao operador autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Campanha criada com sucesso.",
                    content = @Content(schema = @Schema(implementation = CampaignResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou malformados.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de OPERATOR ou ADMIN.", content = @Content)
    })
    public ResponseEntity<CampaignResponseDTO> createCampaign(@Valid @RequestBody CampaignRequestDTO request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        CampaignResponseDTO response = createCampaign.createCampaign(request,user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @PostMapping("{campaignId}/send")
    @Operation(summary = "Disparar uma campanha imediatamente", description = "Inicia o processo de envio imediato das mensagens da campanha para o segmento alvo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Processo de disparo iniciado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campanha não encontrada.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    public ResponseEntity<Void> sendCampaign(
            @PathVariable @Parameter(description = "ID único da campanha a ser enviada", example = "1") Long campaignId,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authUser
    ) {
        sendCampaign.sendCampaign(campaignId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @PostMapping("{campaignId}/schedule")
    @Operation(summary = "Agendar o disparo de uma campanha", description = "Define uma data e hora futuras para o envio automatizado das mensagens da campanha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campanha agendada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Data de agendamento inválida (ex: data no passado).", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campanha não encontrada.", content = @Content)
    })
    public ResponseEntity<Void> scheduleCampaign(
            @PathVariable @Parameter(description = "ID único da campanha a ser agendada", example = "1") Long campaignId,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser authUser, @RequestBody CampaignScheduleRequestDto request
    ){

        scheduleCampaign.scheduleCampaign(campaignId,request,authUser.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID", description = "Retorna os detalhes de uma campanha específica através do seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campanha encontrada com sucesso.",
                    content = @Content(schema = @Schema(implementation = CampaignResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Campanha não encontrada.", content = @Content)
    })
    public ResponseEntity<CampaignResponseDTO> getCampaignById(@PathVariable @Parameter(description = "ID único da campanha", example = "1") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(campaignService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Listar todas as campanhas paginadas", description = "Retorna uma lista paginada de todas as campanhas cadastradas no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de campanhas retornada com sucesso.", content = @Content)
    })
    public ResponseEntity<Page<CampaignResponseDTO>> getAllCampaigns(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user, Pageable pageable) {
        return ResponseEntity.ok(campaignService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Atualizar uma campanha existente", description = "Atualiza as informações de uma campanha existente com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campanha atualizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = CampaignResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campanha não encontrada.", content = @Content)
    })
    public ResponseEntity<CampaignResponseDTO> updateCampaign(
            @PathVariable @Parameter(description = "ID da campanha a ser modificada", example = "1") Long id,
            @Valid @RequestBody CampaignRequestDTO request,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {

        return ResponseEntity.ok(campaignService.updateCampaign(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @Operation(summary = "Excluir logicamente uma campanha", description = "Marca uma campanha como deletada (soft delete) no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Campanha excluída com sucesso (Sem conteúdo de retorno).", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campanha não encontrada.", content = @Content)
    })
    public ResponseEntity<Void> deleteCampaign(@PathVariable @Parameter(description = "ID da campanha a ser excluída", example = "1") Long id,
                                               @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        campaignService.deleteCampaign(id,user.getId());
        return ResponseEntity.noContent().build();
    }
}
