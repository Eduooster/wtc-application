package org.wtc.application.Campaignmetrics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.Campaignmetrics.entity.CampaignMetric;
import org.wtc.application.campaing.service.CreateCampaign;

@RequestMapping("/campaign-metrics")
@RestController
@RequiredArgsConstructor
public class CampaignMetricController {

    private final CreateCampaign campaignService;

    @GetMapping("/{campaignCode}")
    public ResponseEntity<Void> handleCampaignClick(
            @PathVariable String campaignCode,
            @RequestParam Long clientId) {


        String targetUrl = campaignService.processClickAndGetTargetUrl(campaignCode, clientId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, targetUrl)
                .build();
    }
}
