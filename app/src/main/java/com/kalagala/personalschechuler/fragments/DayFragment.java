package com.kalagala.personalschechuler.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kalagala.personalschechuler.R;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DayFragment extends Fragment {
    private static final String TAG ="DayFragment";
    public static Fragment newInstance(){
        return new DayFragment();
    }

    ViewPager2 tasksPager;
    TabLayout daysTab;
    DaysPagerAdapter daysPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return  inflater.inflate(R.layout.frament_show_tasks, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mDaysOfWeek = (LinearLayout) view.findViewById(R.id.days_of_the_week);
        //mTodaysDate =(TextView) view.findViewById(R.id.today_date);
        tasksPager = (ViewPager2) view.findViewById(R.id.tasks_pager);
        daysPagerAdapter = new DaysPagerAdapter(this);
        tasksPager.setAdapter(daysPagerAdapter);

        daysTab = (TabLayout) view.findViewById(R.id.days_tab);

        new TabLayoutMediator(daysTab, tasksPager,
                (tab, position)->{
                    String tabText = getDayAndDate(position);
                    tab.setText(tabText);
                    tasksPager.setCurrentItem(tab.getPosition(), true);
                }
        ).attach();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        day--;
        Log.d(TAG, "it is "+DayOfWeek.of(day)+" acording to calender");
        tasksPager.setCurrentItem(day-1);

//        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//        Date today = calendar.getTime();
//        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMMM yyy");
//        String formattedDate = formatter.format(today);
//        //mTodaysDate.setText(formattedDate);
//        calendar.setTime(today);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//
//        for (int i = 1; i<8; i++){
//            View dayCard = getLayoutInflater().inflate(R.layout.day_of_a_week,null, false);
//            int testResource;
//            Date thisDaysDate;
//            switch (i){
//                case 2:
//                    testResource=R.string.tuesday_short;
//                    thisDaysDate = getThisDaysDate(dayOfWeek, i, today);
//                    break;
//                case 3:
//                    testResource=R.string.wednesday_short;
//                    thisDaysDate = getThisDaysDate(dayOfWeek, i, today);
//                    break;
//                case 4:
//                    testResource=R.string.thursday_short;
//                    thisDaysDate = getThisDaysDate(dayOfWeek, i, today);
//                    break;
//                case 5:
//                    testResource=R.string.friday_short;
//                    thisDaysDate = getThisDaysDate(dayOfWeek, i, today);
//                    break;
//
//                case 6:
//                    testResource=R.string.saturday_short;
//                    thisDaysDate = getThisDaysDate(dayOfWeek, i, today);
//                    break;
//                case 7:
//                    testResource=R.string.sunday_short;
//                    thisDaysDate = getThisDaysDate(dayOfWeek, i, today);
//                    break;
//                default:
//                    testResource=R.string.monday_short;
//                    thisDaysDate = getThisDaysDate(dayOfWeek, i, today);
//            }
////            ((TextView)dayCard.findViewById(R.id.week_day)).setText(testResource);
////            Calendar calendar1 = Calendar.getInstance(TimeZone.getDefault());
////            calendar1.setTime(thisDaysDate);
////            ((TextView)dayCard.findViewById(R.id.day_date)).setText(new Integer(calendar1.get(Calendar.DAY_OF_MONTH)).toString());
////            mDaysOfWeek.addView(dayCard);
//        }

    }

    private String getDayAndDate(int forDayOfTheWeek) {
        Log.d(TAG, "get day and date called with position "+forDayOfTheWeek);
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
//        Log.d(TAG, "first day of Week is "+firstDayOfWeek);
//        Log.d(TAG, "last Day of week is "+lastDayOfWeek);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        Date todayDate = calendar.getTime();
        Log.d(TAG, "\ntoday is "+todayDate);

        calendar.setTime(todayDate);
        int thisDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        thisDayOfTheWeek--;
        Date date = getThisDaysDate(thisDayOfTheWeek, forDayOfTheWeek+1, todayDate);
        Log.d(TAG, "todays day of the week is "+DayOfWeek.of(thisDayOfTheWeek-1));
        Log.d(TAG, "date for day "+DayOfWeek.of(forDayOfTheWeek+1)+" is "+date+"\n");
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd");
        String formattedDate = formatter.format(date);
        return  formattedDate;
    }

    private Date getThisDaysDate(int currentDayOfWeek, int dayToGetDateOf, Date today) {

        Log.d(TAG, "trying to get date for "+DayOfWeek.of(dayToGetDateOf));
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
            Log.d("DayFragment",currentDayOfWeek+"  "+dayToGetDateOf);
            return today;
        }
        return null;
    }

    private class DaysPagerAdapter extends FragmentStateAdapter {
        public DaysPagerAdapter(DayFragment dayFragment) {
            super(dayFragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ShowTasksFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return 7;
        }
    }
}
