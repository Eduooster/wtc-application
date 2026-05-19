package org.wtc.application.campaing.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.campaing.dto.CampaignScheduleRequestDto;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.campaing.enums.CampaignStatus;
import org.wtc.application.campaing.exceptions.CampaignNotFoundException;
import org.wtc.application.campaing.mapper.CampaignMapper;
import org.wtc.application.campaing.repository.CampaignRepository;

import java.time.Instant;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ScheduleCampaign {

    private static final Logger log = LoggerFactory.getLogger(ScheduleCampaign.class);
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final TaskScheduler taskScheduler;
    private final SendCampaign sendCampaign;


    @Transactional
    public CampaignResponseDTO scheduleCampaign(Long id, CampaignScheduleRequestDto request) {

        Campaign campaign = campaignRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campanha não encontrada"));

        campaign.setStatus(CampaignStatus.SCHEDULED);
        campaign.setScheduledAt(request.scheduleAt());

        Campaign savedCampaign = campaignRepository.save(campaign);


        Instant instantGatilho = request.scheduleAt()
                .atZone(ZoneId.systemDefault())
                .toInstant();


        taskScheduler.schedule(
                () -> sendCampaign.sendCampaign(savedCampaign.getId()),
                instantGatilho
        );

        log.info("Campanha alterada e agendada no relógio do sistema: " + savedCampaign.toString());

        return campaignMapper.toDTO(savedCampaign);
    }
}
