package com.kalagala.personalschechuler.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task ORDER BY task_start_time ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task ORDER BY task_start_time ASC")
    List<Task> getAllTasksSync();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("select * from task where task_id=:taskUUIDString")
    Task getTask(String taskUUIDString);
}
