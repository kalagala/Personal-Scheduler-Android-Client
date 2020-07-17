package com.kalagala.personalschechuler;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.time.DayOfWeek;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frament_container_home);
        if (fragment == null){
            fragment = DayFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.frament_container_home, fragment)
                    .commit();
        }
    }
}
