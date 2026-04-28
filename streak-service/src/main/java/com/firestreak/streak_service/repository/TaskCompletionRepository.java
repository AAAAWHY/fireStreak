package com.firestreak.streak_service.repository;

import com.firestreak.streak_service.entity.TaskCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {
    List<TaskCompletion> findAllByUserId(Long userId);
    List<TaskCompletion> findAllByTaskId(Long taskId);
}
