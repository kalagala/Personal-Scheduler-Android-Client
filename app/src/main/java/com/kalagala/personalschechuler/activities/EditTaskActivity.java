package com.kalagala.personalschechuler.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.fragments.EditTaskFragment;

public class EditTaskActivity extends AppCompatActivity {
    public static String UUIDString = "uuidstring";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Bundle args = getIntent().getExtras();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.edit_task_fragment_container);

        if (fragment == null) {

            fragment = EditTaskFragment.newInstance();
            fragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.edit_task_fragment_container, fragment)
                    .commit();
            Log.d("CreateTaskActivity", "creating a creating task fragment");
        }


    }
}
