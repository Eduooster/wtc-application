package org.wtc.application.client.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.participant.Participant;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.tag.entity.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wtc_clients")
@Getter
@Setter
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;


    private String phoneNumber;

    private String companyName;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "client_segments",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "segment_id")
    )
    private List<Segment> segments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "client_tags",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean deleted;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "authenticable_user_id", referencedColumnName = "id")
    private AuthenticableUser credentials;

    @PostPersist
    public void afterSave() {
        if (this.participant != null) {
            this.participant.setRefId(this.id);
        }
    }


}