package com.kalagala.personalschechuler.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class Task {
    private String taskTitle;
    private AlertType alertType;

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

    @PrimaryKey
    @NonNull
    private UUID taskId;
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

    public Task(){
        taskTitle="New Task";
        alertType = AlertType.NOTIFICATION;
        taskRecurrence = TaskRecurrence.DAILY;
        mTaskColor = TaskColor.randomColor();
        taskStartTime = null;
        taskEndTime = null;
        dayOfWeek = null;
        date = null;
        taskId = UUID.randomUUID();

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
}
