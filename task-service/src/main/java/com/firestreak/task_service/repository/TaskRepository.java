package com.firestreak.task_service.repository;

import com.firestreak.task_service.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByUserId(Long userId);

    List<TaskEntity> findAllByFolderId(Long folderId);
}
