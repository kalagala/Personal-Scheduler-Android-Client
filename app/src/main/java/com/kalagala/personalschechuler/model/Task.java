package com.kalagala.personalschechuler.model;

import android.icu.util.Calendar;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;

public class Task {
    private String taskTitle;
    private AlertType alertType;

    public TaskColor getTaskColor() {
        return mTaskColor;
    }

    public void setTaskColor(TaskColor taskColor) {
        mTaskColor = taskColor;
    }

    private TaskColor mTaskColor;
    private Recurrence taskRecurrence;
    private Time taskStartTime;
    private Time taskEndTime;
    private DayOfWeek dayOfWeek;
    private Date date;

    public Task(){
        taskTitle="New Task";
        alertType = AlertType.NOTIFICATION;
        taskRecurrence = Recurrence.ONCE;
        mTaskColor = TaskColor.randomColor();
        taskStartTime = null;
        taskEndTime = null;
        dayOfWeek = null;
        date = null;

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

    public Recurrence getTaskRecurrence() {
        return taskRecurrence;
    }

    public void setTaskRecurrence(Recurrence taskRecurrence) {
        this.taskRecurrence = taskRecurrence;
    }

    public Time getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Time taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public Time getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Time taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
