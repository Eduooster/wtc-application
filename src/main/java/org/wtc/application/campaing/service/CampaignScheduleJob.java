package org.wtc.application.campaing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.campaing.enums.CampaignStatus;
import org.wtc.application.campaing.repository.CampaignRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class CampaignScheduleJob {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private SendCampaign sendCampaign;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void checkAndRunScheduledCampaigns() {
        LocalDateTime now = LocalDateTime.now();

        List<Campaign> campaignsToRun = campaignRepository
                .findByStatusAndScheduledAtBefore(CampaignStatus.SCHEDULED, now);

        for (Campaign campaign : campaignsToRun) {

            campaign.setStatus(CampaignStatus.PROCESSING);
            campaignRepository.save(campaign);


            sendCampaign.sendCampaign(campaign.getId());
        }
    }
}