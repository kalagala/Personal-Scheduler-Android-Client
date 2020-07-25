package com.kalagala.personalschechuler.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.kalagala.personalschechuler.database.AppPersistentData;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskDao;
import com.kalagala.personalschechuler.utils.NotificationHelpers;

import java.util.List;

public class StartupService extends JobIntentService {
    public static String TAG ="StartupService";
    TaskDao taskDao;


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "received an intent to recreate notifications");
        AppPersistentData db = AppPersistentData.getDatabase(getApplicationContext());
        taskDao = db.taskDao();

        List<Task> allTasks = taskDao.getAllTasksSync();

        for (Task task: allTasks){
            Log.d(TAG, "creating notification for task "+task);
            new NotificationHelpers(getApplicationContext(), task).createNotification();
        }
    }


}
