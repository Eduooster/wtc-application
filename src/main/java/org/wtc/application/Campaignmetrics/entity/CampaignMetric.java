package org.wtc.application.Campaignmetrics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.wtc.application.campaing.entity.Campaign;

import java.time.LocalDateTime;

@Entity
@Table(name = "wtc_campaign_metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "campaign_id", nullable = false, unique = true)
    private Campaign campaign;

    @Column(name = "clicks_count", nullable = false)
    private Long clicksCount = 0L;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean deleted;
}