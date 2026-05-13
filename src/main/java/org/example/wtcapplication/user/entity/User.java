package org.example.wtcapplication.user.entity;

import jakarta.persistence.*;
import org.example.wtcapplication.user.enums.UserRole;

@Entity
@Table(name = "wtc_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean active = true;


}
