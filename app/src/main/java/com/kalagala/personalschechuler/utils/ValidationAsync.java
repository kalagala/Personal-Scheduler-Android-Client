package com.kalagala.personalschechuler.utils;


import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.database.TaskRepository;
import com.kalagala.personalschechuler.model.Task;

import java.time.LocalTime;
import java.util.List;

public class ValidationAsync {
    private static final String TAG = "Validation";
    Task newTask;
    Fragment fragment;
    public ValidationAsync(Task newTask, Fragment fragment){
        this.newTask = newTask;
        this.fragment= fragment;
    }
    public boolean checkTimeWindowAvailability() {
        Log.d(TAG,"checking time Window availability");
        (new TaskRepository(
                fragment
                        .getActivity()
                        .getApplication())
                .getAllTasks())
                .observe(
                        fragment.getActivity(),
                        new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                validate(tasks);
            }
        });

        return true;
    }

    boolean validate(List<Task> allTasks){
        Log.d(TAG, "Found "+allTasks.size()+" existing tasks");
        for (Task taskFromDb: allTasks){
            Log.d(TAG, "Checking Conflict With task "+taskFromDb);
            switch (newTask.getTaskRecurrence()){
                case ONLY_ON: return onlyOnTaskHasNoConflictWithExistingTask(taskFromDb);
                case ONCE: return onceOccurringTaskHasNoConflictWithExisting(taskFromDb);
                case DAILY:return dailyOccurringTaskHasNoConflictWithExistingTask(taskFromDb);

                //TODO figure out how to check time conflicts
            }
        }

        return true;

    }
    private boolean dailyOccurringTaskHasNoConflictWithExistingTask(Task taskFromDb) {
        switch (taskFromDb.getTaskRecurrence()){
            case DAILY: return dailyOccurringTaskHasNoConflictWithExistingDailyTask(taskFromDb);
            case ONLY_ON: return dailyOccurringTaskHasNoConflictWithExistingOnlyOnTask(taskFromDb);
            case ONCE: return dailyOccurringTaskHasNoConflictWithExistingOnceTask(taskFromDb);

        }
        return true;
    }

    private boolean dailyOccurringTaskHasNoConflictWithExistingOnceTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }

    private boolean dailyOccurringTaskHasNoConflictWithExistingOnlyOnTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }


    private boolean dailyOccurringTaskHasNoConflictWithExistingDailyTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }

    private boolean onceOccurringTaskHasNoConflictWithExisting(Task taskFromDb) {
        switch (taskFromDb.getTaskRecurrence()){
            case ONCE: return onceOccurringTaskHasNoConflictWithExistingOnceTask(taskFromDb);
            case DAILY: return onceOccurringTaskHasNoConflictWithExistingDailyTask(taskFromDb);
            case ONLY_ON: return onceOccurringTaskHasNoConflictWithExistingOnlyOnTask(taskFromDb);
        }
        return true;
    }

    private boolean onceOccurringTaskHasNoConflictWithExistingOnlyOnTask(Task taskFromDb) {
        if (newTask.getDate().getDayOfWeek().equals(taskFromDb.getDayOfWeek())){
            return hasNoTimeConflict(taskFromDb);
        }
        return true;
    }

    private boolean onceOccurringTaskHasNoConflictWithExistingDailyTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }

    private boolean onceOccurringTaskHasNoConflictWithExistingOnceTask(Task taskFromDb) {
        if (newTask.getDate().compareTo(taskFromDb.getDate()) == 0){
            return hasNoTimeConflict(taskFromDb);
        }
        return true;
    }


    private boolean hasNoTimeConflict(Task taskFromDb) {
        if (newTaskTimeIsBetweenExistingTaskTime(
                newTask.getTaskStartTime(),
                taskFromDb.getTaskStartTime(),
                taskFromDb.getTaskEndTime()
        )) {
//            showInfoDialogWithCustomString(
//                    R.string.start_time_not_available,
//                    R.string.task_available_at_specified_time,
//                    taskFromDb.getTaskTitle()
//            );
            return false;
        } else if (
                newTaskTimeIsBetweenExistingTaskTime(
                        newTask.getTaskEndTime(),
                        taskFromDb.getTaskStartTime(),
                        taskFromDb.getTaskEndTime()
                )) {
//            showInfoDialogWithCustomString(
//                    R.string.task_end_time_not_available,
//                    R.string.task_end_time_available_at_specified_time,
//                    taskFromDb.getTaskTitle()
//            );
            return false;
        }
        return true;
    }
    private boolean onlyOnTaskHasNoConflictWithExistingTask(Task taskFromDb){
        switch (taskFromDb.getTaskRecurrence()){
            case ONLY_ON:
                if (newTask.getDayOfWeek().equals(taskFromDb.getDayOfWeek())){
                    return hasNoTimeConflict(taskFromDb);
                }
            case DAILY:
                return hasNoTimeConflict(taskFromDb);
            case ONCE:
                return onlyOnTaskHasNoConflictWithExistingOnceTask(taskFromDb);
        }
        return true;
    }
    private boolean onlyOnTaskHasNoConflictWithExistingOnceTask(Task taskFromDb) {
        if (newTask.getDayOfWeek().equals(taskFromDb.getDate().getDayOfWeek())){
            return hasNoTimeConflict(taskFromDb);
        }
        return true;
    }
    private boolean newTaskTimeIsBetweenExistingTaskTime(LocalTime newTasktime, LocalTime existingTaskStartTime, LocalTime existingTaskEndTime){
        return ((newTasktime.isAfter(existingTaskStartTime))&&(newTasktime.isBefore(existingTaskEndTime)));

    }

}

