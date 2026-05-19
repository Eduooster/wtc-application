package org.wtc.application.campaing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.enums.CampaignStatus;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wtc_campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_segment_id", nullable = false)
    private Segment targetSegment;

    @OneToMany(mappedBy = "campaign",fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @Column(name = "send_notification", nullable = false)
    private boolean sendNotification = false;

    private LocalDateTime scheduledAt;

    private LocalDateTime sentAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(name = "internal_route", nullable = false)
    private String internalRoute;

    @Column(name = "campaign_code", nullable = false, unique = true)
    private String campaignCode;


    public static Campaign createFromRequest(CampaignRequestDTO request, User creator, Segment segment) {
        Campaign campaign = new Campaign();
        campaign.setTitle(request.getTitle());
        campaign.setContent(request.getContent());
        campaign.setTargetSegment(segment);
        campaign.setCreator(creator);
        campaign.setStatus(CampaignStatus.PENDING);
        campaign.setScheduledAt(request.getScheduledAt());
        campaign.setDeleted(false);
        campaign.setCampaignCode(generateCodeCampaign(request.getTitle()));
        campaign.setInternalRoute(cleanInternalRoute(request.getInternalRoute()));

        return campaign;
    }

    private static String generateCodeCampaign(String title) {
        if (title == null) return "";
        return title.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", "-");
    }

    private static String cleanInternalRoute(String route) {
        if (route != null && route.startsWith("/")) {
            return route.substring(1);
        }
        return route;
    }
}