package org.wtc.application.segment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.client.entity.Client;
import org.wtc.application.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Client> clients = new HashSet<>();
    @ManyToMany(mappedBy = "segments")
    private Set<User> operators = new HashSet<>();

    @OneToMany(mappedBy = "targetSegment")
    private List<Campaign> campaigns = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean deleted = false;
}