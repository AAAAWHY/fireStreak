package com.firestreak.task_service.entity;


import com.firestreak.task_service.enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TaskEntity {
    @Id
    @Column(name = "id",unique = true,nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "frequencyDays", nullable = false)
    Integer frequencyDays;

    @Column(name = "userId", nullable = false)
    Long userId;

    @Column(name = "targetCount")
    Double targetCount;

    @Column(name = "completedCount", nullable = false)
    Double completedCount = 0D;

    @Column(name = "lastCompletedAt")
    LocalDate lastCompletedAt;

    @Column(name = "taskType", nullable = false)
    @Enumerated(value = EnumType.STRING)
    TaskType taskType;

    @Column(name = "folderId")
    Long folderId;
}