package org.wtc.application.tag.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.wtc.application.client.entity.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wtc_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String color;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "tags")
    private List<Client> clients = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Boolean deleted;
}
