package com.kalagala.personalschechuler.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.fragments.CreateTaskFrament;
import com.kalagala.personalschechuler.fragments.EditTaskFrament;

public class EditTaskActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.create_task_fragment_container);
        if (fragment == null){
            fragment = EditTaskFrament.newInstance();
            fm.beginTransaction()
                    .add(R.id.create_task_fragment_container, fragment)
                    .commit();
        }
    }
}
