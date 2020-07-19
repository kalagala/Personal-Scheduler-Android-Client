package com.kalagala.personalschechuler.model.database;

import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskDao;

@Database(entities = {Task.class}, version=1)
@TypeConverters({AppTypeConvertors.class})
public abstract class AppPersistentData extends RoomDatabase {
    public abstract TaskDao userDao();
}
