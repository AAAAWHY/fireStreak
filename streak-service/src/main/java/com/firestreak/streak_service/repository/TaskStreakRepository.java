package com.firestreak.streak_service.repository;

import com.firestreak.streak_service.entity.TaskStreak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskStreakRepository extends JpaRepository<TaskStreak, Long> {
    List<TaskStreak> findAllByUserId(Long userId);
}