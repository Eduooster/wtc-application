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



@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final ICampaignService campaignService;
    private final CreateCampaign createCampaign;
    private final SendCampaign sendCampaign;
    private final ScheduleCampaign scheduleCampaign;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<CampaignResponseDTO> createCampaign(@Valid @RequestBody CampaignRequestDTO request,@AuthenticationPrincipal AuthenticableUser user) {
        CampaignResponseDTO response = createCampaign.createCampaign(request,user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @PostMapping("{campaignId}/send")
    public ResponseEntity<Void> sendCampaign(
            @PathVariable Long campaignId,
            @AuthenticationPrincipal AuthenticableUser authUser
    ) {
        sendCampaign.sendCampaign(campaignId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    @PostMapping("{campaignId}/schedule")
    public ResponseEntity<Void> scheduleCampaign(
            @PathVariable Long campaignId,
            @AuthenticationPrincipal AuthenticableUser authUser,@RequestBody CampaignScheduleRequestDto request
            ){

        scheduleCampaign.scheduleCampaign(campaignId,request);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")

    public ResponseEntity<CampaignResponseDTO> getCampaignById(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(campaignService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<Page<CampaignResponseDTO>> getAllCampaigns(@AuthenticationPrincipal AuthenticableUser user, Pageable pageable) {
        return ResponseEntity.ok(campaignService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<CampaignResponseDTO> updateCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CampaignRequestDTO request,
            @AuthenticationPrincipal AuthenticableUser user) {

        return ResponseEntity.ok(campaignService.updateCampaign(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id,
                                               @AuthenticationPrincipal AuthenticableUser user) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
