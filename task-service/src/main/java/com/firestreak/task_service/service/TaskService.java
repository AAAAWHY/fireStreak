package com.firestreak.task_service.service;

import com.firestreak.task_service.dto.CreateTaskRequest;
import com.firestreak.task_service.dto.TaskDto;
import com.firestreak.task_service.dto.UpdateTaskRequest;
import com.firestreak.task_service.entity.TaskEntity;
import com.firestreak.task_service.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    void createTask(CreateTaskRequest createTaskRequest){
        TaskEntity taskEntity = TaskEntity.builder()
                .title(createTaskRequest.title())
                .description(createTaskRequest.description())
                .frequencyMinutes(createTaskRequest.frequencyMinutes())
                .taskType(createTaskRequest.taskType())
                .folderId(createTaskRequest.folderId())
                .targetCount(createTaskRequest.targetCount())
                .userId((Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).build();
        taskRepository.save(taskEntity);
    }

    void deleteTask(long taskId){

    }

    List<TaskDto> getAllTasksOfUser(Long userId){

        return null;
    }

    void completeTask(Long taskId, double count){

    }

    void updateTask(UpdateTaskRequest updateTaskDto){

    }
}