package org.wtc.application.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wtc.application.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "wtc_audits")
@Data

@NoArgsConstructor
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

    public Audit(String action,
                 String details,
                 User author,
                 Long entityId,
                 String entityName,
                 Boolean deleted) {
        this.action = action;
        this.details = details;
        this.author = author;
        this.entityId = entityId;
        this.entityName = entityName;
        this.deleted = deleted;
        this.createAt = LocalDateTime.now();
    }


}
