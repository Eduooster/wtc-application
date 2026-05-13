package org.example.wtcapplication.metrics.entity;

import jakarta.persistence.*;
import org.example.wtcapplication.campaing.entity.Campaign;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
public class Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalSent = 0;

    private Integer totalOpened = 0;

    private Integer totalClicked = 0;

    private Integer totalFailed = 0;

    @OneToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters e Setters
}