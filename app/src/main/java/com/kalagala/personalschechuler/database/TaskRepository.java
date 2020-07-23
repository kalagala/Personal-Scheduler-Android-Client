package com.kalagala.personalschechuler.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskDao;

import java.util.List;

public class TaskRepository {
    private static final String TAG = "TaskRepository";

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application){
        AppPersistentData db = AppPersistentData.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
        
    }


    public LiveData<List<Task>> getAllTasks() {
        if (allTasks.getValue() != null){
            for (Task task: allTasks.getValue()){
                Log.d(TAG, "here is a task from db "+task);
            }
        }

        return allTasks;
    }


    public void insert(Task task){

       new insertAsyncTask(taskDao).execute(task);
    }

    public Task getTask(String taskUUIDString) {
        return taskDao.getTask(taskUUIDString);
    }

    public void updateTask(Task task) {
    }

    public void deleteTask(Task task) {
        new deleteAsyncTask(taskDao).execute(task);
    }

    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        deleteAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks[0]);
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        insertAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks[0]);
            return null;
        }
    }


    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        updateAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks[0]);
            return null;
        }
    }

}
