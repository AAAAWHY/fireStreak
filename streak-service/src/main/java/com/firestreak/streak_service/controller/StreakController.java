package com.firestreak.streak_service.controller;

import com.firestreak.streak_service.dto.TaskCompletionDto;
import com.firestreak.streak_service.dto.TaskStreakDto;
import com.firestreak.streak_service.dto.UserStreakDto;
import com.firestreak.streak_service.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class StreakController {
    private final StreakService streakService;

    @GetMapping("/streaks/user/tasks")
    List<TaskStreakDto> getTaskStreaksOfUser(){
        return streakService.getTaskStreaksOfUser();
    }

    @GetMapping("/streaks/user")
    UserStreakDto getUserStreak(){
        return streakService.getUserStreak();
    }

    @GetMapping("/streaks/task/{id}")
    TaskStreakDto getTaskStreak(@PathVariable("id") Long taskId){
        return streakService.getTaskStreak(taskId);
    }

    @GetMapping("/completions/user")
    List<TaskCompletionDto> getTaskCompletionsOfUser(){
        return streakService.getTaskCompletionsOfUser();
    }

    @GetMapping("/completions/task/{id}")
    List<TaskCompletionDto> getTaskCompletionsOfTask(@PathVariable("id") Long taskId){
        return streakService.getTaskCompletionsOfTask(taskId);
    }
}