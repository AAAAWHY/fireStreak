package com.firestreak.flame_service.repository;

import com.firestreak.flame_service.entity.UserCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserCatRepository extends JpaRepository<UserCat, Long> {
    List<UserCat> findAllByUserId(Long userId);
}