package com.firestreak.streak_service.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TaskStreak {
    @Id
    @Column(name = "taskId", nullable = false,unique = true)
    Long taskId;

    @Column(name = "streakCount", nullable = false)
    Long streakCount;

    @Column(name = "userId", nullable = false)
    Long userId;

    @Column(name = "lastCompletedAt")
    LocalDateTime lastCompletedAt;
}
