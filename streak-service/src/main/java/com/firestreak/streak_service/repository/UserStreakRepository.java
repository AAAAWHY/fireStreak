package com.firestreak.streak_service.repository;

import com.firestreak.streak_service.entity.UserStreak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStreakRepository extends JpaRepository<UserStreak, Long> {
}
