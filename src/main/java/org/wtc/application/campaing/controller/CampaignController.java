package org.wtc.application.campaing.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.campaing.service.ICampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final ICampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignResponseDTO> createCampaign(@Valid @RequestBody CampaignRequestDTO request,@AuthenticationPrincipal AuthenticableUser user) {
        log.debug("Request to create a campaign: {}", request);
        CampaignResponseDTO response = campaignService.createCampaign(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponseDTO> getCampaignById(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(campaignService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponseDTO>> getAllCampaigns(@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(campaignService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponseDTO> updateCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CampaignRequestDTO request,
            @AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id,
                                               @AuthenticationPrincipal AuthenticableUser user) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
