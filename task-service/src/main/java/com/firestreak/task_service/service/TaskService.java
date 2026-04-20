package com.firestreak.task_service.service;

import com.firestreak.task_service.dto.CreateTaskRequest;
import com.firestreak.task_service.dto.TaskDto;
import com.firestreak.task_service.dto.UpdateTaskRequest;
import com.firestreak.task_service.entity.TaskEntity;
import com.firestreak.task_service.enums.TaskType;
import com.firestreak.task_service.repository.TaskRepository;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.config.NamespaceUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
        if(createTaskRequest.taskType().equals(TaskType.QUANTITATIVE) &&
                (createTaskRequest.targetCount() == null || createTaskRequest.targetCount() == 0))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "targetCount required for quantitative task");
        taskRepository.save(taskEntity);
    }

    void deleteTask(long taskId){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()-> {
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "null user");
        });
        if(!task.getUserId().equals(userId))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong user");
        taskRepository.deleteById(taskId);
    }

    List<TaskDto> getAllTasksOfUser(Long userId){
        List<TaskEntity> tasks = taskRepository.findAllByUserId(userId);
        return tasks.stream().map(TaskDto::from).toList();
    }

    void completeTask(Long taskId, double count){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()-> {
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "null user");
        });
        if(!task.getUserId().equals(userId))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong user");
        task.setCompletedCount(task.getCompletedCount()+count);
        taskRepository.save(task);
    }

    void updateTask(UpdateTaskRequest updateTaskDto, long taskId){
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()->{
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cant find task with this id");
        });
        if (updateTaskDto.description() != null) task.setDescription(updateTaskDto.description());

        if(updateTaskDto.taskType() != null)
            if(updateTaskDto.taskType().equals(TaskType.QUANTITATIVE) &&
                    (updateTaskDto.targetCount() == null || updateTaskDto.targetCount() == 0))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "targetCount required for quantitative task");

        if(updateTaskDto.frequencyMinutes() != null) task.setFrequencyMinutes(updateTaskDto.frequencyMinutes());
        if(updateTaskDto.targetCount() != null) task.setTargetCount(updateTaskDto.targetCount());
        if(updateTaskDto.title() != null) task.setTitle(updateTaskDto.title());
        if(updateTaskDto.folderId() != null) task.setFolderId(updateTaskDto.folderId());
        taskRepository.save(task);
    }
}