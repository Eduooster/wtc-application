package org.example.wtcapplication.campaing.entity;

import jakarta.persistence.*;
import org.example.wtcapplication.campaing.enums.CampaignStatus;
import org.example.wtcapplication.segment.entity.Segment;
import org.example.wtcapplication.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "wtc_campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "segment_id")
    private Segment targetSegment;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters e Setters
}