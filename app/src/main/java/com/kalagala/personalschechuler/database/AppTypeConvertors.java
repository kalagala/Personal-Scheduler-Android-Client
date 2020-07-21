package com.kalagala.personalschechuler.database;

import androidx.room.TypeConverter;

import com.kalagala.personalschechuler.model.AlertType;
import com.kalagala.personalschechuler.model.TaskRecurrence;
import com.kalagala.personalschechuler.model.TaskColor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.UUID;

public class AppTypeConvertors {
    @TypeConverter
    public static int alertTypeToInt(AlertType alertType){
        return alertType.getId();
    }

    @TypeConverter
    public static AlertType intToAlertType(int alertId){
        return AlertType.getAlertById(alertId);
    }

    @TypeConverter
    public static int taskColorToInt(TaskColor taskColor){
        return taskColor.getColorId();
    }

    @TypeConverter
    public static TaskColor intToTaskColor(int colorId){
        return TaskColor.getTaskColorWithId(colorId);
    }

    @TypeConverter
    public static int taskRecuranceToInt(TaskRecurrence recurrence){
        return recurrence.getId();
    }

    @TypeConverter
    public static TaskRecurrence intToTaskRecurance(int recuranceId){
        return TaskRecurrence.getRecuranceById(recuranceId);
    }

    @TypeConverter
    public static String uuidToString(UUID uuid){
        return uuid.toString();
    }

    @TypeConverter
    public static UUID stringToUUID(String uuid){
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public static long localTimeToLong(LocalTime localTime){
        return localTime.getLong(ChronoField.SECOND_OF_DAY);
    }

    @TypeConverter
    public static LocalTime longToLocalTime(long secondOfDay){
        return LocalTime.ofSecondOfDay(secondOfDay);
    }

    @TypeConverter
    public static  long localDateToLong(LocalDate localDate){
        return localDate.toEpochDay();
    }

    @TypeConverter
    public static LocalDate longToLocalDate(long longDate){
        return LocalDate.ofEpochDay(longDate);
    }

    @TypeConverter
    public static int dayOfWeekToInt(DayOfWeek dayOfWeek){
        return dayOfWeek.getValue();
    }

    @TypeConverter
    public static DayOfWeek intToWeekOfDay(int dayOWeek){
        return  DayOfWeek.of(dayOWeek);
    }
}
