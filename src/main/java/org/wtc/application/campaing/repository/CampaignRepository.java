package org.wtc.application.campaing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.campaing.entity.Campaign;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByIdAndDeletedFalse(Long id);

    List<Campaign> findAllByDeletedFalse();
}
