package org.example.wtcapplication.message.entitity;

import jakarta.persistence.*;
import org.example.wtcapplication.campaing.entity.Campaign;
import org.example.wtcapplication.client.entity.Client;
import org.example.wtcapplication.message.enums.MessageStatus;
import org.example.wtcapplication.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private LocalDateTime sentAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client recipient;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;


}
