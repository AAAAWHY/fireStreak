package com.firestreak.flame_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWallet {
    @Id
    @Column(name="userId", nullable = false)
    Long userId;

    @Column(name = "flameCount", nullable = false)
    @Builder.Default
    Double flames = 0D;

    @Column(name = "purrCount", nullable = false)
    @Builder.Default
    Long purrs = 0L;

    @Column(name = "lootBoxGrade", nullable = false)
    @Builder.Default
    Integer lootBoxGrade = 1;

    @Column(name = "flamesIncome", nullable = false)
    @Builder.Default
    Double flamesIncome = 0D;

    @Column(name = "catsActive", nullable = false)
    @Builder.Default
    Boolean catsActive = true;
}