package com.firestreak.flame_service.entity;

import com.firestreak.flame_service.enums.Rarity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    Long id;

    @Column(name = "baseProductivity", nullable = false)
    Long baseProductivity;

    @Column(name = "baseUpgradeCost", nullable = false)
    Long baseUpgradeCost;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "rarity", nullable = false)
    @Enumerated(EnumType.STRING)
    Rarity rarity;

    @Column(name = "spriteName", nullable = false)
    String spriteName;

    @Column(name = "animationName", nullable = false)
    String animationName;
}
