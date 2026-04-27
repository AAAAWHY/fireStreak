package com.firestreak.streak_service.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserStreak {
    @Id
    @Column(name = "userId", nullable = false, unique = true)
    Long userId;

    @Column(name = "streakCount", nullable = false)
    Integer streakCount;

    @Column(name = "lastCompletedAt")
    LocalDate lastCompletedAt;
}