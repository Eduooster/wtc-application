package org.wtc.application.campaing.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.Campaignmetrics.CampaignMetricRepository;
import org.wtc.application.Campaignmetrics.entity.CampaignMetric;
import org.wtc.application.audit.entity.Audit;
import org.wtc.application.audit.repository.AuditRepository;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.campaing.entity.Campaign;

import org.wtc.application.campaing.repository.CampaignRepository;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.repository.SegmentRepository;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CreateCampaign {

    private final UserRepository userRepository;
    private final SegmentRepository segmentRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignMetricRepository campaignMetricRepository;
    private final AuditRepository auditRepository;



    @Transactional
    public CampaignResponseDTO createCampaign(
            CampaignRequestDTO request,
            AuthenticableUser authUser
    ) {

        User creator = userRepository.findByCredentials(authUser)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found"));

        Segment segment = segmentRepository.findById(request.getTargetSegmentId())
                .orElseThrow(() -> new EntityNotFoundException("Segment not found"));



        Campaign campaign = Campaign.createFromRequest(request, creator, segment);




        campaign.setInternalRoute(campaign.getInternalRoute());


        Campaign savedCampaign = campaignRepository.save(campaign);


        CampaignMetric metric = new CampaignMetric();
        metric.setCampaign(savedCampaign);
        metric.setClicksCount(0L);
        campaignMetricRepository.save(metric);

        auditRepository.save(
                new Audit(
                        "CAMPAIGN_CREATED",
                        "Campaign created " ,
                        creator
                        ,
                        savedCampaign.getId(),
                        "Campaign",
                        false
                )
        );

        return new CampaignResponseDTO(savedCampaign);
    }
    private String generateCodeCampaign(CampaignRequestDTO request) {
        String generatedCode = request.getTitle()
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", "-");

        return generatedCode;
    }

    @Transactional
    public String processClickAndGetTargetUrl(String campaignCode, Long clientId) {
        Campaign campaign = campaignRepository.findByCampaignCode(campaignCode)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));

        CampaignMetric metric = campaignMetricRepository.findByCampaignId(campaign.getId())
                .orElseThrow(() -> new RuntimeException("Métricas não encontradas"));


        metric.setClicksCount(metric.getClicksCount() + 1);
        campaignMetricRepository.save(metric);


        return "http://localhost:8080/campaigns/" + campaign.getId();
    }


}
