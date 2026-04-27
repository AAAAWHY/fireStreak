package com.firestreak.streak_service.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TaskCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    Long id;

    @Column(name = "count", nullable = false)
    Double completedCount;

    @Column(name = "taskId", nullable = false)
    Long taskId;

    @Column(name = "userId", nullable = false)
    Long userId;

    @Column(name = "lastCompletedAt", nullable = false)
    LocalDate lastCompletedAt;
}