package com.kalagala.personalschechuler;

import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.kalagala.personalschechuler.database.AppPersistentData;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskDao;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReminderServices extends IntentService {
    public static String TAG = "ReminderServices";
    TaskDao taskDao;
    List<Task> allTasks;
    DayOfWeek dayOfWeek;


    public ReminderServices() {
        super(TAG);
    }

    public static Intent newIntent(Context context){
        return new  Intent(context, ReminderServices.class);
    }

    public static void setReminders(Context context){
        Intent intent = ReminderServices.newIntent(context);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppPersistentData db = AppPersistentData.getDatabase(this);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasksSync();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date todayDate = calendar.getTime();
        calendar.setTime(todayDate);
        int thisDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        thisDayOfTheWeek--;
        dayOfWeek = DayOfWeek.of(thisDayOfTheWeek);
        LocalDate finalDate  = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Task> thisDayTasks = new ArrayList<>();
        for (Task task: allTasks){
            switch (task.getTaskRecurrence()){
                case DAILY:
                    thisDayTasks.add(task);
                    break;
                case ONCE:
                    if (task.getDate().getDayOfWeek().getValue()==dayOfWeek.getValue() && finalDate.compareTo(task.getDate())==0 ){
                        thisDayTasks.add(task);
                    }
                    break;
                case ONLY_ON:
                    if (task.getDayOfWeek().getValue() == dayOfWeek.getValue()){
                        thisDayTasks.add(task);
                    }
            }

        }

        Log.d(TAG, "it is "+dayOfWeek+" "+todayDate.toString()+" and we are about to set a bunch of notifications ");

    }
}

