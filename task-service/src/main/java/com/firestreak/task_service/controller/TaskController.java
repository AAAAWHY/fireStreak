package com.firestreak.task_service.controller;

import com.firestreak.task_service.dto.*;
import com.firestreak.task_service.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create")
    TaskDto createTask(@RequestBody @Valid CreateTaskRequest request){
        return taskService.createTask(request);
    }

    @DeleteMapping("/delete/{taskId}")
    void delete(@PathVariable("taskId") Long taskId){
        taskService.deleteTask(taskId);
    }

    @GetMapping("/get-all")
    List<TaskDto> getTasks(){
        return taskService.getAllTasksOfUser();
    }

    @PostMapping("/complete/{taskId}")
    CompleteTaskResponse completeTask(@PathVariable("taskId") Long taskId,
                                      @RequestBody CompleteTaskDto completeTaskDto){
        return taskService.completeTask(taskId,completeTaskDto);
    }

    @PatchMapping("/update/{taskId}")
    TaskDto updateTask(@PathVariable("taskId") Long taskId,
                    @RequestBody @Valid UpdateTaskRequest request){
        return taskService.updateTask(request, taskId);
    }

    @GetMapping("/get/{taskId}")
    TaskDto getTask(@PathVariable("taskId") Long taskId){
        return taskService.getTask(taskId);
    }
}