package org.example.wtcapplication.segment.entity;

import jakarta.persistence.*;
import org.example.wtcapplication.client.entity.Client;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "wtc_segments")
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    //@ManyToMany(mappedBy = "segments")
    //private Set<Client> clients = new HashSet<>();
}
