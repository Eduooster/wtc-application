package org.wtc.application.segment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.client.entity.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wtc_segments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToMany(mappedBy = "segments")
    private List<Client> clients = new ArrayList<>();

    @OneToMany(mappedBy = "targetSegment")
    private List<Campaign> campaigns = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Boolean deleted;
}