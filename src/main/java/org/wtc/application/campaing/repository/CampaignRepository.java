package org.wtc.application.campaing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.campaing.enums.CampaignStatus;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByIdAndDeletedFalse(Long id);

    Page<Campaign> findAllByDeletedFalse(Pageable pageable);

    Optional<Campaign> findByCampaignCode(String campaignCode);

    List<Campaign> findByStatusAndScheduledAtBefore(CampaignStatus campaignStatus, LocalDateTime agora);
}
