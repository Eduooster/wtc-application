package org.wtc.application.audit.entity;

import jakarta.persistence.*;
import org.wtc.application.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "wtc_audits")

public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT")
    private String details;

    private LocalDateTime createAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private Long entityId;

    private String entityName;

    private Boolean deleted;


}
