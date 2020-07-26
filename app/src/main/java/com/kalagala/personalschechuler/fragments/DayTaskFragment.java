package com.kalagala.personalschechuler.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.fragments.daysofweek.ShowFridayTasksFragment;
import com.kalagala.personalschechuler.fragments.daysofweek.ShowMondayTasksFragment;
import com.kalagala.personalschechuler.fragments.daysofweek.ShowSaturdayTasksFragment;
import com.kalagala.personalschechuler.fragments.daysofweek.ShowSundayTasksFragment;
import com.kalagala.personalschechuler.fragments.daysofweek.ShowThursdayTasksFragment;
import com.kalagala.personalschechuler.fragments.daysofweek.ShowTuesdayTasksFragment;
import com.kalagala.personalschechuler.fragments.daysofweek.ShowWednesdayTasksFragment;
import com.kalagala.personalschechuler.helpers.DateHelpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DayTaskFragment extends Fragment {
    private static final String TAG ="DayFragment";
    public static Fragment newInstance(){
        return new DayTaskFragment();
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
                    String tabText = getDayAndDate(position+1);
                    tab.setText(tabText);
                    tasksPager.setCurrentItem(tab.getPosition(), true);
                }
        ).attach();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        day--;
        tasksPager.setCurrentItem(day);

    }

    private String getDayAndDate(int forDayOfTheWeek) {

        Date date = new DateHelpers().getThisDaysDate(forDayOfTheWeek);
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd");
        String formattedDate = formatter.format(date);
        return  formattedDate;
    }



    private class DaysPagerAdapter extends FragmentStateAdapter {
        public DaysPagerAdapter(DayTaskFragment dayTaskFragment) {
            super(dayTaskFragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            Date todayDate = calendar.getTime();
            Log.d(TAG, "\ntoday is "+todayDate);

            calendar.setTime(todayDate);

            Date date = new DateHelpers().getThisDaysDate(position+1);
            switch (position){
                case 0:
                    Log.d(TAG, "creating sunday tasks");
                    return ShowSundayTasksFragment.newInstance(position, date);
                case 1:
                    Log.d(TAG, "creating monday task");
                    return ShowMondayTasksFragment.newInstance(position, date);
                case 2:
                    Log.d(TAG, "creating tuesday task");
                    return ShowTuesdayTasksFragment.newInstance(position, date);
                case 3:
                    Log.d(TAG, "creating wednesday task");
                    return ShowWednesdayTasksFragment.newInstance(position, date);
                case 4:
                    Log.d(TAG, "creating thursday task");
                    return ShowThursdayTasksFragment.newInstance(position, date);
                case 5:
                    Log.d(TAG, "creating friday task");
                    return ShowFridayTasksFragment.newInstance(position, date);
                case 6:
                    Log.d(TAG, "creating saturday task");
                    return ShowSaturdayTasksFragment.newInstance(position, date);

                default: return null;
            }

        }

        @Override
        public int getItemCount() {
            return 7;
        }
    }
}
