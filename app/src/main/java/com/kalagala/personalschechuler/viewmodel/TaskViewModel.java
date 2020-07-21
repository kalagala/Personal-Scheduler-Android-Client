package com.kalagala.personalschechuler.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.database.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private static final String TAG = "TaskViewModel";
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(Application application){
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task){
        Log.d(TAG, "about to insert a task to db "+task);
        repository.insert(task);
    }
}
