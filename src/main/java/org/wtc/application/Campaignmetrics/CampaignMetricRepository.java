package org.wtc.application.Campaignmetrics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.Campaignmetrics.entity.CampaignMetric;


import java.util.Optional;



public interface CampaignMetricRepository extends JpaRepository<CampaignMetric, Long> {

    Optional<CampaignMetric> findByCampaignId(Long campaignId);
}
