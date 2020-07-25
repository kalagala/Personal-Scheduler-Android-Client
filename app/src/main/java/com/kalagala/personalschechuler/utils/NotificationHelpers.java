package com.kalagala.personalschechuler.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kalagala.personalschechuler.NotificationReceicer;
import com.kalagala.personalschechuler.model.AlertType;
import com.kalagala.personalschechuler.model.Task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationHelpers {
    private static final String TAG = "NotificationHelper";
    private Context context;
    private Task task;

    public NotificationHelpers(Context context, Task task){
        this.context = context;
        this.task = task;
    }

    public void createNotification() {
        Intent intent = new Intent(
                context.getApplicationContext(),
                NotificationReceicer.class
        );
        Log.d(TAG, "creating a notification with id "+task.getNotificationId());
        intent.putExtra(NotificationReceicer.TITLE, task.getTaskTitle());
        intent.putExtra(NotificationReceicer.TASK_ID, task.getNotificationId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(),
                task.getNotificationId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        setAppropriateAlarm(pendingIntent);
    }


    public void deleteNotification() {
        Log.d(TAG, "Deleting a notification with id "+task.getNotificationId());
        Intent intent = new Intent(
                context.getApplicationContext(),
                NotificationReceicer.class
        );
        intent.putExtra(NotificationReceicer.TITLE, task.getTaskTitle());
        intent.putExtra(NotificationReceicer.TASK_ID, task.getNotificationId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(),
                task.getNotificationId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    private void setAppropriateAlarm(PendingIntent pendingIntent) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        switch (task.getTaskRecurrence()){
            case ONLY_ON:
                if (task.getAlertType() == AlertType.NOTIFICATION){
                    DayOfWeek taskDayOfWeek = task.getDayOfWeek();
                    calendar.set(Calendar.DAY_OF_WEEK, taskDayOfWeek.getValue());
                    calendar.set(Calendar.HOUR_OF_DAY, task.getTaskStartTime().getHour());
                    calendar.set(Calendar.MINUTE, task.getTaskStartTime().getMinute());
                    calendar.set(Calendar.SECOND, 0);
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY*7,
                            pendingIntent

                    );
                }
                break;
            case ONCE:
                if (task.getAlertType() == AlertType.NOTIFICATION ){
                    LocalDate taskDate = task.getDate();
                    Date date = Date.from(taskDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR_OF_DAY, task.getTaskStartTime().getHour());
                    calendar.set(Calendar.MINUTE, task.getTaskStartTime().getMinute());
                    calendar.set(Calendar.SECOND, 0);
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY*7,
                            pendingIntent

                    );
                }
                break;
            case DAILY:
                if (task.getAlertType() == AlertType.NOTIFICATION){
                    calendar.set(Calendar.HOUR_OF_DAY, task.getTaskStartTime().getHour());
                    calendar.set(Calendar.MINUTE, task.getTaskStartTime().getMinute());
                    calendar.set(Calendar.SECOND, 0);
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent

                    );
                }

                break;
        }
    }

}
