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
import com.kalagala.personalschechuler.model.ValidationResponse;

import java.time.LocalTime;
import java.util.List;

public class ValidationAsync {
    private static final String TAG = "Validation";
    Task newTask;
    public ValidationAsync(Task newTask){
        this.newTask = newTask;
    }
    public ValidationResponse validate(List<Task> allTasksFromDb){
        Log.d(TAG, "validation Started");
        Log.d(TAG, "Found "+allTasksFromDb.size()+" existing tasks");
        int iteration =0;
        ValidationResponse validationResponse;
        for (Task taskFromDb: allTasksFromDb){
            Log.d(TAG, "iteration "+iteration);
            iteration++;
            Log.d(TAG, "Checking if \n"+newTask+"\n has Conflict With taskfrom db \n"+taskFromDb);
            switch (newTask.getTaskRecurrence()){
                case ONLY_ON:
                    Log.d(TAG, "validating an only on task against tasks from db");
                    if(!onlyOnTaskHasNoConflictWithExistingTask(taskFromDb).isValid()){
                        return onlyOnTaskHasNoConflictWithExistingTask(taskFromDb);
                    }
                    break;
                case ONCE:
                    Log.d(TAG, "validating a once task against tasks from db");
                    if(!onceOccurringTaskHasNoConflictWithExisting(taskFromDb).isValid()){
                        return onceOccurringTaskHasNoConflictWithExisting(taskFromDb);
                    }
                    break;

                case DAILY:
                    Log.d(TAG, "validating a daily task against tasks from db");
                    if(!dailyOccurringTaskHasNoConflictWithExistingTask(taskFromDb).isValid()){
                        return dailyOccurringTaskHasNoConflictWithExistingTask(taskFromDb);
                    }
                    break;

            }
        }

        return new ValidationResponse(true);

    }
    private ValidationResponse dailyOccurringTaskHasNoConflictWithExistingTask(Task taskFromDb) {
        switch (taskFromDb.getTaskRecurrence()){
            case DAILY:
                Log.d(TAG, "checking weather a daily occurring task  has querals with daily task from db");
                return dailyOccurringTaskHasNoConflictWithExistingDailyTask(taskFromDb);
            case ONLY_ON:
                Log.d(TAG, "checking weather a daily occurring task  has querals with only on task from db");
                return dailyOccurringTaskHasNoConflictWithExistingOnlyOnTask(taskFromDb);
            case ONCE:
                Log.d(TAG, "checking weather a daily occurring task  has querals with once task from db");
                return dailyOccurringTaskHasNoConflictWithExistingOnceTask(taskFromDb);

        }
        return new ValidationResponse(true);
    }

    private ValidationResponse dailyOccurringTaskHasNoConflictWithExistingOnceTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }

    private ValidationResponse dailyOccurringTaskHasNoConflictWithExistingOnlyOnTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }


    private ValidationResponse dailyOccurringTaskHasNoConflictWithExistingDailyTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }

    private ValidationResponse onceOccurringTaskHasNoConflictWithExisting(Task taskFromDb) {
        switch (taskFromDb.getTaskRecurrence()){
            case ONCE:
                Log.d(TAG, "checking weather once occurring task  has querals with once task from db");
                return onceOccurringTaskHasNoConflictWithExistingOnceTask(taskFromDb);
            case DAILY:
                Log.d(TAG, "checking weather once occurring task  has querals with daily task from db");
                return onceOccurringTaskHasNoConflictWithExistingDailyTask(taskFromDb);
            case ONLY_ON:
                Log.d(TAG, "checking weather once occurring task  has querals with only on task from db");
                return onceOccurringTaskHasNoConflictWithExistingOnlyOnTask(taskFromDb);
        }
        return new ValidationResponse(true);
    }

    private ValidationResponse onceOccurringTaskHasNoConflictWithExistingOnlyOnTask(Task taskFromDb) {
        if (newTask.getDate().getDayOfWeek().getValue()==taskFromDb.getDayOfWeek().getValue()){
            return hasNoTimeConflict(taskFromDb);
        }
        return new ValidationResponse(true);
    }

    private ValidationResponse onceOccurringTaskHasNoConflictWithExistingDailyTask(Task taskFromDb) {
        return hasNoTimeConflict(taskFromDb);
    }

    private ValidationResponse onceOccurringTaskHasNoConflictWithExistingOnceTask(Task taskFromDb) {
        if (newTask.getDate().compareTo(taskFromDb.getDate()) == 0){
            return hasNoTimeConflict(taskFromDb);
        }
        return new ValidationResponse(true);
    }


    private ValidationResponse hasNoTimeConflict(Task taskFromDb) {
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
            return new ValidationResponse(
                    false,
                    R.string.start_time_not_available,
                    R.string.task_available_at_specified_time,
                    taskFromDb.getTaskTitle()


            );
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
            return new ValidationResponse(
                    false,
                    R.string.task_end_time_not_available,
                    R.string.task_end_time_available_at_specified_time,
                    taskFromDb.getTaskTitle()
            );
        }
        return new ValidationResponse(true);
    }
    private ValidationResponse onlyOnTaskHasNoConflictWithExistingTask(Task taskFromDb){
        switch (taskFromDb.getTaskRecurrence()){
            case ONLY_ON:
                Log.d(TAG, "checking wheather an only has querals with only on task from db");
                if (newTask.getDayOfWeek().getValue()==taskFromDb.getDayOfWeek().getValue()){
                    return hasNoTimeConflict(taskFromDb);
                }
                return new ValidationResponse(true);
            case DAILY:
                Log.d(TAG, "checking wheather an only on task has querals with daily task from db");
                return hasNoTimeConflict(taskFromDb);
            case ONCE:
                Log.d(TAG, "checking wheather an only on task has querals with once occuring task from db");
                return onlyOnTaskHasNoConflictWithExistingOnceTask(taskFromDb);
        }
        return new ValidationResponse(true);
    }
    private ValidationResponse onlyOnTaskHasNoConflictWithExistingOnceTask(Task taskFromDb) {
        if (newTask.getDayOfWeek().equals(taskFromDb.getDate().getDayOfWeek())){
            return hasNoTimeConflict(taskFromDb);
        }
        return new ValidationResponse(true);
    }
    private boolean newTaskTimeIsBetweenExistingTaskTime(LocalTime newTasktime, LocalTime existingTaskStartTime, LocalTime existingTaskEndTime){
        return ((newTasktime.isAfter(existingTaskStartTime))&&(newTasktime.isBefore(existingTaskEndTime)));

    }

}

