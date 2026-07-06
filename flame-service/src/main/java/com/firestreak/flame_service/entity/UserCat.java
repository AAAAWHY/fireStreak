package com.firestreak.flame_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    Long id;

    @Column(name = "userId", nullable = false)
    Long userId;

    @Column(name = "catCardId", nullable = false)
    Long catCardId;

    @Column(name = "lvl", nullable = false)
    @Builder.Default
    Long lvl = 1L;
}