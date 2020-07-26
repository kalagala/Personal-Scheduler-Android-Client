package com.kalagala.personalschechuler.helpers;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.ValidationResponse;

import java.time.LocalDate;

public class ValidationSync {
    Task newTask;

    public ValidationSync(Task newTask){
        this.newTask = newTask;
    }
    public ValidationResponse taskDateHasBeenEntered() {
        if (!newTask.taskDateHasBeenEnteredByUser()){
            //showInfoDialog(R.string.empty_date, R.string.empty_date_message);
            return new ValidationResponse(
                    false,
                    R.string.empty_date,
                    R.string.empty_date_message
            );
        }else if (newTask.getDate().compareTo(LocalDate.now())<0){
            return new ValidationResponse(
                    false,
                    R.string.invalid_date,
                    R.string.date_in_the_past
            );
        }
        return new ValidationResponse(true);
    }
    public ValidationResponse checkTaskEndTime() {
        //TODO  check if time is available
        if (!newTask.taskEndTimeHasBeenEnteredByUser()){
            //showInfoDialog(R.string.set_task_end_time, R.string.no_end_time_message);
            return new ValidationResponse(
                    false,
                    R.string.set_task_end_time,
                    R.string.no_end_time_message
            );
        }else if (newTask.getTaskEndTime().compareTo(newTask.getTaskStartTime()) == 0){

            return new ValidationResponse(
                    false,
                    R.string.invalid_time,
                    R.string.same_end_time_message
            );
        }else if (newTask.getTaskEndTime().compareTo(newTask.getTaskStartTime()) < 0){
            return new ValidationResponse(
                    false,
                    R.string.invalid_time,
                    R.string.task_end_time_before_start
            );
        }
        return new ValidationResponse(true);
    }
    public ValidationResponse checkTaskStartTime() {
        if (!newTask.taskStartTimeHasBeenEnteredByUser()){
            return new ValidationResponse(
                    false,
                    R.string.set_task_start_time,
                    R.string.no_start_time_message
            );
        }
        return new ValidationResponse(true);
    }
    public ValidationResponse checkTaskTitle() {
        if (newTask.getTaskTitle().equals("") || newTask.getTaskTitle().equals("New Task")){

            return new ValidationResponse(
                    false,
                    R.string.set_task_title,
                    R.string.no_task_title_message
            );
        }else {
            return new ValidationResponse(true);
        }
    }

}
