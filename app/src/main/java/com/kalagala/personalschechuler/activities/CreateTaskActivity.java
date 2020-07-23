package com.kalagala.personalschechuler.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kalagala.personalschechuler.fragments.CreateTaskFrament;
import com.kalagala.personalschechuler.R;

public class CreateTaskActivity extends AppCompatActivity {
//
    public static final String ACTIVITY_PURPOSE="Activity_Purpose";
    public static final int ARG_EDIT_TASK = 1;
    public static final int ARG_CREATE_TASK =2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Bundle args = getIntent().getExtras();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.create_task_fragment_container);

        if (fragment == null){
            if (args.getInt(ACTIVITY_PURPOSE)== ARG_CREATE_TASK){
                fragment = CreateTaskFrament.newInstance();
                fm.beginTransaction()
                        .add(R.id.create_task_fragment_container, fragment)
                        .commit();
                Log.d("CreateTaskActivity", "creating a creating task fragment");
            }else {
//                fragment = CreateTaskFrament.newInstance();
//                Log.d("CreateTaskActivity", "creating an edittask fragment");
            }



        }
    }
}
