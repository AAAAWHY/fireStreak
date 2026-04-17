package com.firestreak.task_service.repository;

import com.firestreak.task_service.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    List<FolderEntity> findAllByUserId(Long userId);
}
