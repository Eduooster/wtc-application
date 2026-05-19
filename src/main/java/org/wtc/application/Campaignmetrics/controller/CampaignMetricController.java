package org.wtc.application.Campaignmetrics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.Campaignmetrics.entity.CampaignMetric;
import org.wtc.application.campaing.service.CreateCampaign;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/campaign-metrics")
@RestController
@RequiredArgsConstructor
@Tag(name = "Métricas de Campanha", description = "Endpoints para rastreamento de engajamento, cliques e comportamento dos clientes nas campanhas.")
public class CampaignMetricController {

    private final CreateCampaign campaignService;

    @GetMapping("/{campaignCode}")
    @Operation(
            summary = "Processar clique na campanha",
            description = "Registra a métrica de clique do cliente na campanha informada e redireciona o navegador do usuário para a URL de destino da campanha."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Redirecionamento executado com sucesso. A URL final está presente no cabeçalho 'Location'.",
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URL de destino para onde o cliente será redirecionado.", schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string")),
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Código de campanha ou ID do cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> handleCampaignClick(
            @PathVariable @Parameter(description = "Código único identificador da campanha", example = "CAMP-2026-X") String campaignCode,
            @RequestParam @Parameter(description = "ID do cliente que realizou o clique", example = "123") Long clientId) {

        String targetUrl = campaignService.processClickAndGetTargetUrl(campaignCode, clientId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, targetUrl)
                .build();
    }
}