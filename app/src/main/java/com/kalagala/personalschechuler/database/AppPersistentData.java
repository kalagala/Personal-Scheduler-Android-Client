package com.kalagala.personalschechuler.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.TypeConverters;

import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskDao;

@Database(entities = {Task.class}, version=4)
@TypeConverters({AppTypeConvertors.class})
public abstract class AppPersistentData extends RoomDatabase {
    private static AppPersistentData INSTANCE;
    public abstract TaskDao taskDao();

    public static AppPersistentData getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppPersistentData.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppPersistentData.class, "task")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }
}
