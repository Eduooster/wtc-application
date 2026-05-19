package org.wtc.application.campaing.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.audit.entity.Audit;
import org.wtc.application.audit.repository.AuditRepository;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.campaing.dto.CampaignScheduleRequestDto;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.campaing.enums.CampaignStatus;
import org.wtc.application.campaing.exceptions.CampaignNotFoundException;
import org.wtc.application.campaing.mapper.CampaignMapper;
import org.wtc.application.campaing.repository.CampaignRepository;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ScheduleCampaign {

    private static final Logger log = LoggerFactory.getLogger(ScheduleCampaign.class);
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final TaskScheduler taskScheduler;
    private final SendCampaign sendCampaign;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;


    @Transactional
    public CampaignResponseDTO scheduleCampaign(Long id, CampaignScheduleRequestDto request,Long idUser) {

        Campaign campaign = campaignRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campanha não encontrada"));


        User user = userRepository.findById(idUser).orElseThrow(()->new EntityNotFoundException("User not found"));

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


        auditRepository.save(
                new Audit(
                        "CAMPAIGN_SCHEDULED",
                        "Campaign scheduled for " + request.scheduleAt(),
                        user,
                        savedCampaign.getId(),
                        "Campaign",
                        false
                )
        );

        return campaignMapper.toDTO(savedCampaign);
    }
}
