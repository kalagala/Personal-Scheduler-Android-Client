package com.kalagala.personalschechuler.helpers;

import android.util.Log;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateHelpers {
    private String TAG = "DateHelpers";
    Calendar calendar;
    Date today;
    public DateHelpers(){
        calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        today = new Date();
    }

    public Date getThisDaysDate(int dayToGetDateOf) {
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "trying to get date for "+ DayOfWeek.of(dayToGetDateOf));
        Log.d(TAG, "today is "+DayOfWeek.of(currentDayOfWeek));
        if (currentDayOfWeek>dayToGetDateOf){
            for (int j = currentDayOfWeek; j>=dayToGetDateOf; j--){
                if (j==dayToGetDateOf){
                    return new Date(today.getTime() - ((currentDayOfWeek-dayToGetDateOf)*24 * 3600000));
                }
            }
        }else if(currentDayOfWeek<dayToGetDateOf){
            for (int j = currentDayOfWeek; j<=dayToGetDateOf; j++){
                if (j==dayToGetDateOf){
                    return new Date(today.getTime() + ((dayToGetDateOf-currentDayOfWeek)*24 * 3600000));
                }
            }
        }else{
            Log.d(TAG,currentDayOfWeek+"  "+dayToGetDateOf);
            return today;
        }
        return null;
    }
}
