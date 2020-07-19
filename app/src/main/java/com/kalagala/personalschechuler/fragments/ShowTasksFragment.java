package com.kalagala.personalschechuler.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.Utils;
import com.kalagala.personalschechuler.activities.CreateTaskActivity;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShowTasksFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mNoTasksTextview;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private List<Task> mTasks = new ArrayList<>();
    public static ShowTasksFragment newInstance(){
        return new ShowTasksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskAdapter = new TaskAdapter(getActivity());
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                Log.d("ShowTask", "found "+tasks.size()+" tasks from livedata");
                taskAdapter.setTasks(tasks);
                mTasks = tasks;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
//         for(int i=0; i<10; i++){
//            tasks.add(new Task());
//         }
         View view =inflater.inflate(R.layout.day_tasks_fragment, container, false);
         mRecyclerView =(RecyclerView) view.findViewById(R.id.tasks_container);
         mNoTasksTextview = (TextView) view.findViewById(R.id.not_task_text);
            taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(List<Task> tasks) {
                    if (tasks.size()!=0){
                        mNoTasksTextview.setVisibility(View.INVISIBLE);
                    }
                }
            });

         mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
         mRecyclerView.setAdapter(taskAdapter);
         return  view;

    }




    class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
        List<Task> mTasks;
        LayoutInflater layoutInflater;

//        TaskAdapter(List<Task> tasks){
//            mTasks = tasks;
//        }

        TaskAdapter(Context context){
            layoutInflater.from(context);

        }

        void setTasks(List<Task> tasks){
            mTasks = tasks;
            notifyDataSetChanged();
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
            holder.bindTask(ShowTasksFragment.this.mTasks.get(position));
        }

        @Override
        public int getItemCount() {
            if (mTasks != null){
                return ShowTasksFragment.this.mTasks.size();
            }else {
                return 0;
            }

        }
    }

    public  class TaskHolder extends RecyclerView.ViewHolder{
        CardView taskCard;
        TextView mTaskTitle;
        TextView mStartEndTime;
        ImageView alertType;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
                    Bundle args = new Bundle();
                    args.putInt(CreateTaskActivity.ACTIVITY_PURPOSE, CreateTaskActivity.ARG_EDIT_TASK);
                    intent.putExtras(args);
                    startActivity(intent);
                }
            });
            mTaskTitle =(TextView) itemView.findViewById(R.id.task_title);
            mStartEndTime =(TextView) itemView.findViewById(R.id.start_end_time);
            alertType = (ImageView) itemView.findViewById(R.id.alert_type_icon);
            taskCard = (CardView) itemView.findViewById(R.id.task_card);
        }

        void bindTask(Task task){
            mTaskTitle.setText(task.getTaskTitle());
//            String startTime = task.getTaskStartTime().toString();
//            mStartEndTime.setText(startTime);

            taskCard.setCardBackgroundColor(Utils.getColorResourceForTaskColor(task.getTaskColor()));

        }
    }
}
