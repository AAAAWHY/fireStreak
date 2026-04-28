package com.firestreak.task_service.service;

import com.firestreak.task_service.config.RabbitMqConfig;
import com.firestreak.task_service.dto.CompleteTaskDto;
import com.firestreak.task_service.dto.CreateTaskRequest;
import com.firestreak.task_service.dto.TaskDto;
import com.firestreak.task_service.dto.UpdateTaskRequest;
import com.firestreak.task_service.entity.TaskEntity;
import com.firestreak.task_service.enums.TaskType;
import com.firestreak.task_service.exception.TaskAccessDeniedException;
import com.firestreak.task_service.exception.TaskNotFoundException;
import com.firestreak.task_service.exception.TaskValidationException;
import com.firestreak.task_service.messaging.TaskCompletedEvent;
import com.firestreak.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final RabbitTemplate rabbitTemplate;

    public TaskDto createTask(CreateTaskRequest createTaskRequest){
        if(createTaskRequest.taskType().equals(TaskType.QUANTITATIVE) &&
                (createTaskRequest.targetCount() == null || createTaskRequest.targetCount() == 0))
            throw new TaskValidationException("targetCount required for quantitative task");

        TaskEntity taskEntity = TaskEntity.builder()
                .title(createTaskRequest.title())
                .description(createTaskRequest.description())
                .frequencyDays(createTaskRequest.frequencyDays())
                .taskType(createTaskRequest.taskType())
                .folderId(createTaskRequest.folderId())
                .targetCount(createTaskRequest.targetCount())
                .completedCount(0d)
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

    public TaskDto completeTask(Long taskId, CompleteTaskDto completeTaskDto){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()-> {
            return new TaskNotFoundException(taskId);
        });
        if(!task.getUserId().equals(userId))
            throw new TaskAccessDeniedException(taskId);
        Double count;
        if(task.getTaskType().equals(TaskType.BOOLEAN))
            count = 1D;
        else{
            count = completeTaskDto.count();
            if(count == null)
                throw new TaskValidationException("Completed count is required");
        }

        task.setCompletedCount(task.getCompletedCount()+count);
        task.setLastCompletedAt(LocalDate.now());
        task = taskRepository.save(task);
        if(task.getTaskType().equals(TaskType.BOOLEAN) ||
                (task.getTaskType().equals(TaskType.QUANTITATIVE) && task.getCompletedCount()>=task.getTargetCount())) {
            TaskCompletedEvent taskCompletedEvent = TaskCompletedEvent.builder()
                    .completedAt(LocalDate.now())
                    .taskId(taskId)
                    .userId(userId)
                    .count(count)
                    .frequencyDays(task.getFrequencyDays())
                    .build();
            rabbitTemplate.convertAndSend(RabbitMqConfig.TASK_EXCHANGE, RabbitMqConfig.TASK_COMPLETED_ROUTING_KEY, taskCompletedEvent);
        }
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
        if(updateTaskDto.frequencyDays() != null) task.setFrequencyDays(updateTaskDto.frequencyDays());
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