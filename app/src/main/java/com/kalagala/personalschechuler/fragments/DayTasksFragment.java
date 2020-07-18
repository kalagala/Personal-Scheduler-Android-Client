package com.kalagala.personalschechuler.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.activities.EditTaskActivity;
import com.kalagala.personalschechuler.model.Task;

import java.util.ArrayList;
import java.util.List;

public class DayTasksFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Task> tasks= new ArrayList<>();
    public static DayTasksFragment newInstance(){
        return new DayTasksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         for(int i=0; i<10; i++){
            tasks.add(new Task());
         }
         View view =inflater.inflate(R.layout.day_tasks_fragment, container, false);
         mRecyclerView =(RecyclerView) view.findViewById(R.id.tasks_container);
         mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
         mRecyclerView.setAdapter(new TasksAdapter(tasks));
         return  view;

    }




    class TasksAdapter extends RecyclerView.Adapter<TaskHolder>{
        List<Task> mTasks;

        TasksAdapter(List<Task> tasks){
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            holder.bindTask(tasks.get(position));
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }

    public  class TaskHolder extends RecyclerView.ViewHolder{
        TextView mTaskTitle;
        TextView mStartEndTime;
        ImageView alertType;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                    startActivity(intent);
                }
            });
            mTaskTitle =(TextView) itemView.findViewById(R.id.task_title);
            mStartEndTime =(TextView) itemView.findViewById(R.id.start_end_time);
            alertType = (ImageView) itemView.findViewById(R.id.alert_type_icon);
        }

        void bindTask(Task task){
            mTaskTitle.setText(task.getTaskTitle());
//            String startTime = task.getTaskStartTime().toString();
//            mStartEndTime.setText(startTime);
        }
    }
}
