package com.firestreak.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    Long id;


    @Column(name = "login", unique = true, nullable = false)
    String login;


    @Column(name = "passwordHash", nullable = false)
    String passwordHash;


    @Column(name = "username", nullable = false)
    String username;


    @Column(name = "email", nullable = false)
    String email;


    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    Role role;
}
