package org.wtc.application.Campaignmetrics.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.wtc.application.Campaignmetrics.CampaignMetricRepository;
import org.wtc.application.Campaignmetrics.entity.CampaignMetric;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.campaing.repository.CampaignRepository;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.repository.SegmentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CampaignMetricService {

    private final CampaignRepository campaignRepository;
    private final CampaignMetricRepository campaignMetricRepository;
    private final SegmentRepository segmentRepository;



    @Transactional
    public CampaignResponseDTO createCampaign(CampaignRequestDTO request, AuthenticableUser user) {

        Segment segment = segmentRepository.findById(request.getTargetSegmentId())
                .orElseThrow(() -> new EntityNotFoundException("Segment not found"));

        Campaign campaign = new Campaign();

        String generatedCode = request.getTitle()
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", "-");

        campaign.setCampaignCode(generatedCode);
        campaign.setTargetSegment(segment);
        campaign.setInternalRoute(request.getInternalRoute());


        Campaign savedCampaign = campaignRepository.save(campaign);


        CampaignMetric metric = new CampaignMetric();
        metric.setCampaign(savedCampaign);
        metric.setClicksCount(0L);
        campaignMetricRepository.save(metric);

        return toResponseDTO(savedCampaign);
    }


    @Transactional
    public String processClickAndGetTargetUrl(String campaignCode, Long clientId) {

        Campaign campaign = campaignRepository.findByCampaignCode(campaignCode)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));


        CampaignMetric metric = campaignMetricRepository.findByCampaignId(campaign.getId())
                .orElseThrow(() -> new RuntimeException("Métricas da campanha não encontradas"));


        metric.setClicksCount(metric.getClicksCount() + 1);
        campaignMetricRepository.save(metric);


        return "granafacil://" + campaign.getInternalRoute() + "?utm_client=" + clientId;
    }

    private CampaignResponseDTO toResponseDTO(Campaign campaign) {

        return new CampaignResponseDTO(
                campaign
        );
    }
}
