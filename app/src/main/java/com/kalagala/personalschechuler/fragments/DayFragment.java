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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DayFragment extends Fragment {
    public static Fragment newInstance(){
        return new DayFragment();
    }
    LinearLayout mDaysOfWeek;
    ViewPager2 tasksPager;
    TabLayout daysTab;
    DaysPagerAdapter daysPagerAdapter;
    TextView mTodaysDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return  inflater.inflate(R.layout.frament_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDaysOfWeek = (LinearLayout) view.findViewById(R.id.days_of_the_week);
        mTodaysDate =(TextView) view.findViewById(R.id.today_date);
        tasksPager = (ViewPager2) view.findViewById(R.id.tasks_pager);
        daysTab = (TabLayout) view.findViewById(R.id.days_tab);
        new TabLayoutMediator(daysTab, tasksPager,(tab, position)->{
            String tabText = getDayAndDate(position);
            tab.setText(tabText);
            tasksPager.setCurrentItem(tab.getPosition(), true);
        });
        daysTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tasksPager.setCurrentItem(tab.getPosition());
                Log.d("TAB", "tab clicked "+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        daysPagerAdapter = new DaysPagerAdapter(this);
        tasksPager.setAdapter(daysPagerAdapter);


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
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date today = calendar.getTime();
        calendar.setTime(today);
        int thisDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Date date = getThisDaysDate(thisDayOfTheWeek, forDayOfTheWeek, today);
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd");
        String formattedDate = formatter.format(today);
        return  formattedDate;
    }

    private Date getThisDaysDate(int currentDayOfWeek, int dayToGetDateOf, Date today) {
        if (currentDayOfWeek>dayToGetDateOf){
            for (int j = currentDayOfWeek; j>=dayToGetDateOf; j--){
                if (j==dayToGetDateOf){
                    return new Date(today.getTime() - ((currentDayOfWeek-dayToGetDateOf)*24 * 3600000));
                }
            }
        }else if(currentDayOfWeek<dayToGetDateOf){
            for (int j = currentDayOfWeek; j<=dayToGetDateOf; j++){
                if (j==dayToGetDateOf){
                    return new Date(today.getTime() + ((currentDayOfWeek-dayToGetDateOf)*24 * 3600000));
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
            return ShowTasksFragment.newInstance();
        }

        @Override
        public int getItemCount() {
            return 7;
        }
    }
}
