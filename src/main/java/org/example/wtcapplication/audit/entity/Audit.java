package org.example.wtcapplication.audit.entity;

import jakarta.persistence.*;
import org.example.wtcapplication.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "audits")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT")
    private String details;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private Long entityId;

    private String entityName;


}
