package org.wtc.application.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.participant.Participant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wtc_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Boolean active = true;



    @OneToMany(mappedBy = "creator")
    private List<Campaign> campaigns = new ArrayList<>();

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