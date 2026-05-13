package org.example.wtcapplication.client.entity;

import jakarta.persistence.*;
import org.example.wtcapplication.segment.entity.Segment;
import org.example.wtcapplication.tag.entity.Tag;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String fcmToken;

    @ManyToMany
    @JoinTable(
            name = "client_tags",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "client_segments",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "segment_id")
    )
    private Set<Segment> segments = new HashSet<>();

    // Getters e Setters
}