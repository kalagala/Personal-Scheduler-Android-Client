package com.kalagala.personalschechuler.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "task_id")
    private UUID taskId;

    @ColumnInfo(name = "task_name")
    private String taskTitle;

    @ColumnInfo(name = "task_alert_type")
    private AlertType alertType;

    @ColumnInfo(name = "task_color")
    private TaskColor mTaskColor;

    @ColumnInfo(name = "task_recurance")
    private TaskRecurrence taskRecurrence;

    @ColumnInfo(name = "task_start_time")
    private LocalTime taskStartTime;

    @ColumnInfo(name = "task_end_time")
    private LocalTime taskEndTime;

    @ColumnInfo(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @ColumnInfo(name = "date")
    private LocalDate date;

    private boolean taskStartTimeHasBeenEnteredByUser;
    private boolean taskEndTimeHasBeenEnteredByUser;
    private boolean taskDateHasBeenEnteredByUser;

    public Task(Task task){
        taskTitle = task.taskTitle;
        taskId = task.taskId;
        alertType = task.alertType;
        taskRecurrence = task.taskRecurrence;
        mTaskColor = task.mTaskColor;
        taskStartTime = task.taskStartTime;
        taskEndTime = task.taskEndTime;
        dayOfWeek = task.dayOfWeek;
        date = task.date;
        taskDateHasBeenEnteredByUser = task.taskDateHasBeenEnteredByUser;
        taskEndTimeHasBeenEnteredByUser = task.taskEndTimeHasBeenEnteredByUser;
        taskStartTimeHasBeenEnteredByUser = task.taskStartTimeHasBeenEnteredByUser;

    }
    public Task(){
        taskTitle="New Task";
        alertType = AlertType.NOTIFICATION;
        taskRecurrence = TaskRecurrence.DAILY;
        mTaskColor = TaskColor.PURPLE;
        taskStartTime = LocalTime.now();
        taskEndTime = LocalTime.now();
        dayOfWeek = DayOfWeek.MONDAY;
        date = LocalDate.now();
        taskId = UUID.randomUUID();
        taskDateHasBeenEnteredByUser = false;
        taskEndTimeHasBeenEnteredByUser = false;
        taskStartTimeHasBeenEnteredByUser = false;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                ", alertType=" + alertType +
                ", mTaskColor=" + mTaskColor +
                ", taskRecurrence=" + taskRecurrence +
                ", taskStartTime=" + taskStartTime +
                ", taskEndTime=" + taskEndTime +
                ", dayOfWeek=" + dayOfWeek +
                ", date=" + date +
                '}';
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public TaskColor getmTaskColor() {
        return mTaskColor;
    }

    public void setmTaskColor(TaskColor mTaskColor) {
        this.mTaskColor = mTaskColor;
    }


    public TaskColor getTaskColor() {
        return mTaskColor;
    }

    public void setTaskColor(TaskColor taskColor) {
        mTaskColor = taskColor;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public TaskRecurrence getTaskRecurrence() {
        return taskRecurrence;
    }

    public void setTaskRecurrence(TaskRecurrence taskRecurrence) {
        this.taskRecurrence = taskRecurrence;
    }

    public LocalTime getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(LocalTime taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public LocalTime getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(LocalTime taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTaskRecurrenceToRecuranceWithId(int position) {
        this.taskRecurrence = TaskRecurrence.getRecuranceById(position);
    }

    public boolean taskStartTimeHasBeenEnteredByUser() {
        return taskStartTimeHasBeenEnteredByUser;
    }

    public void setTaskStartTimeHasBeenEnteredByUser(boolean taskStartTimeHasBeenEnteredByUser) {
        this.taskStartTimeHasBeenEnteredByUser = taskStartTimeHasBeenEnteredByUser;
    }

    public boolean taskEndTimeHasBeenEnteredByUser() {
        return taskEndTimeHasBeenEnteredByUser;
    }

    public void setTaskEndTimeHasBeenEnteredByUser(boolean taskEndTimeHasBeenEnteredByUser) {
        this.taskEndTimeHasBeenEnteredByUser = taskEndTimeHasBeenEnteredByUser;
    }

    public boolean taskDateHasBeenEnteredByUser() {
        return taskDateHasBeenEnteredByUser;
    }

    public void setTaskDateHasBeenEnteredByUser(boolean taskDateHasBeenEnteredByUser) {
        this.taskDateHasBeenEnteredByUser = taskDateHasBeenEnteredByUser;
    }
}
