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

    @Column(nullable = false)
    private Integer totalSent = 0;

    @Column(nullable = false)
    private Integer totalOpened = 0;

    @Column(nullable = false)
    private Integer totalFailed = 0;

    @OneToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean deleted;
}