package com.firestreak.task_service.service;

import com.firestreak.task_service.dto.CreateTaskRequest;
import com.firestreak.task_service.dto.TaskDto;
import com.firestreak.task_service.dto.UpdateTaskRequest;
import com.firestreak.task_service.entity.TaskEntity;
import com.firestreak.task_service.enums.TaskType;
import com.firestreak.task_service.exception.TaskAccessDeniedException;
import com.firestreak.task_service.exception.TaskNotFoundException;
import com.firestreak.task_service.exception.TaskValidationException;
import com.firestreak.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskDto createTask(CreateTaskRequest createTaskRequest){
        if(createTaskRequest.taskType().equals(TaskType.QUANTITATIVE) &&
                (createTaskRequest.targetCount() == null || createTaskRequest.targetCount() == 0))
            throw new TaskValidationException("targetCount required for quantitative task");

        TaskEntity taskEntity = TaskEntity.builder()
                .title(createTaskRequest.title())
                .description(createTaskRequest.description())
                .frequencyMinutes(createTaskRequest.frequencyMinutes())
                .taskType(createTaskRequest.taskType())
                .folderId(createTaskRequest.folderId())
                .targetCount(createTaskRequest.targetCount())
                .userId((Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).build();
        taskEntity = taskRepository.save(taskEntity);
        return TaskDto.from(taskEntity);
    }

    public void deleteTask(long taskId){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()-> {
            return new TaskNotFoundException(taskId);
        });
        if(!task.getUserId().equals(userId))
            throw new TaskAccessDeniedException(taskId);
        taskRepository.deleteById(taskId);
    }

    public List<TaskDto> getAllTasksOfUser(){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TaskEntity> tasks = taskRepository.findAllByUserId(userId);
        return tasks.stream().map(TaskDto::from).toList();
    }

    public TaskDto completeTask(Long taskId, double count){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()-> {
            return new TaskNotFoundException(taskId);
        });
        if(!task.getUserId().equals(userId))
            throw new TaskAccessDeniedException(taskId);
        task.setCompletedCount(task.getCompletedCount()+count);
        task.setLastCompletedAt(LocalDateTime.now());
        taskRepository.save(task);
        return TaskDto.from(task);
    }

    public TaskDto updateTask(UpdateTaskRequest updateTaskDto, long taskId){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()->{
            return new TaskNotFoundException(taskId);
        });
        if(!task.getUserId().equals(userId))
            throw new TaskAccessDeniedException(taskId);



        if (updateTaskDto.description() != null) task.setDescription(updateTaskDto.description());
        if(updateTaskDto.taskType() != null)
            if(updateTaskDto.taskType().equals(TaskType.QUANTITATIVE) &&
                    (updateTaskDto.targetCount() == null || updateTaskDto.targetCount() == 0))
                throw new TaskValidationException("targetCount required for quantitative task");
        if(updateTaskDto.frequencyMinutes() != null) task.setFrequencyMinutes(updateTaskDto.frequencyMinutes());
        if(updateTaskDto.targetCount() != null) task.setTargetCount(updateTaskDto.targetCount());
        if(updateTaskDto.title() != null) task.setTitle(updateTaskDto.title());
        if(updateTaskDto.folderId() != null) task.setFolderId(updateTaskDto.folderId());
        taskRepository.save(task);
        return TaskDto.from(task);
    }

    public TaskDto getTask(Long taskId){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()-> {
            return new TaskNotFoundException(taskId);
        });
        if(!task.getUserId().equals(userId))
            throw new TaskAccessDeniedException(taskId);
        return TaskDto.from(task);
    }
}