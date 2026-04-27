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
import java.util.List;

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

        int lastCompletedPeriod = (int) ChronoUnit.DAYS.between(taskStreak.getStreakStartDate(), taskStreak.getLastCompletedAt()) / taskCompletedEvent.frequencyDays();
        int curCompletedPeriod = (int) ChronoUnit.DAYS.between(taskStreak.getStreakStartDate(), taskCompletedEvent.completedAt()) / taskCompletedEvent.frequencyDays();
        int periodDiff = curCompletedPeriod-lastCompletedPeriod;

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


    public void everyDayCheck(){
        List<UserStreak> userStreaks = userStreakRepository.findAll();
        for(UserStreak userStreak : userStreaks){
            checkUserStreak(userStreak);
        }
    }















    private void checkUserStreak(UserStreak userStreak){
        List<TaskStreak> taskStreaks = taskStreakRepository.findAllByUserId(userStreak.getUserId());
        boolean allTasksCompleted = true;
        boolean atLeasOneCompleted = false;
        for(TaskStreak taskStreak : taskStreaks){
            if(checkTaskStreak(taskStreak))
                atLeasOneCompleted = true;
            else allTasksCompleted = false;
            taskStreakRepository.save(taskStreak);
        }
        if(allTasksCompleted)
            userStreak.setHardStreakCount(userStreak.getHardStreakCount()+1);
        if(atLeasOneCompleted)
            userStreak.setStreakCount(userStreak.getStreakCount()+1);
        userStreakRepository.save(userStreak);
    }

    private boolean checkTaskStreak(TaskStreak taskStreak){
        int lastCompletedPeriod = (int) ChronoUnit.DAYS.between(taskStreak.getStreakStartDate(), taskStreak.getLastCompletedAt()) / taskStreak.getFrequencyDays();
        int curPeriod = (int) ChronoUnit.DAYS.between(taskStreak.getStreakStartDate(), LocalDate.now()) / taskStreak.getFrequencyDays();
        int periodDiff = curPeriod-lastCompletedPeriod;
        if(periodDiff>=2){
            if(taskStreak.getFreezeStreakCount() > 0){
                taskStreak.setFreezeStreakCount(taskStreak.getFreezeStreakCount()-1);
                return true;
            }
            taskStreak.setStreakCount(0);
            return false;
        }
        return true;
    }












    private UserStreak createInitialUserStreak(TaskCompletedEvent taskCompletedEvent){
        UserStreak userStreak = UserStreak.builder()
                .streakCount(1)
                .lastCompletedAt(taskCompletedEvent.completedAt())
                .userId(taskCompletedEvent.userId())
                .build();
        TaskStreak taskStreak = TaskStreak.builder()
                .streakCount(1)
                .freezeStreakCount(0)
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
                .freezeStreakCount(0)
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