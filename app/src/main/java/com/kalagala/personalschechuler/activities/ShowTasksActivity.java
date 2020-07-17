package com.kalagala.personalschechuler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.fragments.DayFragment;

public class ShowTasksActivity extends AppCompatActivity {
    FloatingActionButton createTaskButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createTaskButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowTasksActivity.this, CreateTaskActivity.class);
                startActivity(intent);
            }
        });
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
