package com.firestreak.streak_service.service;

import com.firestreak.streak_service.entity.TaskStreak;
import com.firestreak.streak_service.entity.UserStreak;
import com.firestreak.streak_service.messaging.TaskCompletedEvent;
import com.firestreak.streak_service.repository.TaskStreakRepository;
import com.firestreak.streak_service.repository.UserStreakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class StreakService {
    private final TaskStreakRepository taskStreakRepository;
    private final UserStreakRepository userStreakRepository;



    public void handleCompletedTask(TaskCompletedEvent taskCompletedEvent){
        TaskStreak taskStreak;

        if(!userStreakRepository.existsById(taskCompletedEvent.userId())){
            createInitialUserStreak(taskCompletedEvent);
            return;
        }

        if(!taskStreakRepository.existsById(taskCompletedEvent.taskId())){
            createInitialTaskStreak(taskCompletedEvent);
            return;
        }
        taskStreak = taskStreakRepository.findById(taskCompletedEvent.taskId()).get();

        int lastPeriodCompleted = (int) ChronoUnit.DAYS.between(taskStreak.getStreakStartDate(), taskStreak.getLastCompletedAt()) / taskCompletedEvent.frequencyDays();
        int curPeriodCompleted = (int) ChronoUnit.DAYS.between(taskStreak.getStreakStartDate(), taskCompletedEvent.completedAt()) / taskCompletedEvent.frequencyDays();
        int periodDiff = curPeriodCompleted-lastPeriodCompleted;

        if(periodDiff == 1) {
            taskStreak.setStreakCount(taskStreak.getStreakCount() + 1);
        }
        else if(periodDiff == 0){
            taskStreak.setFreezeStreakCount(taskStreak.getFreezeStreakCount() + 1);
        }
        else{
            taskStreak.setStreakCount(1);
            taskStreak.setStreakStartDate(taskCompletedEvent.completedAt());
        }
        taskStreak.setLastCompletedAt(taskCompletedEvent.completedAt());
        taskStreakRepository.save(taskStreak);
    }




























    private UserStreak createInitialUserStreak(TaskCompletedEvent taskCompletedEvent){
        UserStreak userStreak = UserStreak.builder()
                .streakCount(1)
                .lastCompletedAt(taskCompletedEvent.completedAt())
                .userId(taskCompletedEvent.userId())
                .build();
        TaskStreak taskStreak = TaskStreak.builder()
                .streakCount(1)
                .lastCompletedAt(taskCompletedEvent.completedAt())
                .taskId(taskCompletedEvent.taskId())
                .userId(taskCompletedEvent.userId())
                .streakStartDate(LocalDate.now())
                .frequencyDays(taskCompletedEvent.frequencyDays())
                .build();
        taskStreakRepository.save(taskStreak);
        userStreak = userStreakRepository.save(userStreak);
        return userStreak;
    }


    private TaskStreak createInitialTaskStreak(TaskCompletedEvent taskCompletedEvent){
        TaskStreak taskStreak = TaskStreak.builder()
                .streakCount(1)
                .lastCompletedAt(taskCompletedEvent.completedAt())
                .taskId(taskCompletedEvent.taskId())
                .userId(taskCompletedEvent.userId())
                .frequencyDays(taskCompletedEvent.frequencyDays())
                .streakStartDate(LocalDate.now())
                .build();
        taskStreak = taskStreakRepository.save(taskStreak);
        return taskStreak;
    }
}