package com.kalagala.personalschechuler.fragments.daysofweek;

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
import com.kalagala.personalschechuler.activities.CreateTaskActivity;
import com.kalagala.personalschechuler.activities.EditTaskActivity;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ShowSundayTasksFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mNoTasksTextview;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;


    public static final String DAY_OF_WEEK_ARG = "DAY_OF_WEEK";
    public static final String TAG = "ShowTasksFragment";

    public static final String DATE = "DATE";
    private DayOfWeek dayOfWeek;
    private List<Task> mTasks = new ArrayList<>();
    public static ShowSundayTasksFragment newInstance(int dayOfWeek, Date date){
        Bundle args = new Bundle();
        args.putString(DATE, date.toString());
        ShowSundayTasksFragment showSundayTasksFragment = new ShowSundayTasksFragment();
        showSundayTasksFragment.setArguments(args);
        return  showSundayTasksFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dayOfWeek = DayOfWeek.SUNDAY;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date todayDate = calendar.getTime();
        calendar.setTime(todayDate);
        int thisDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        thisDayOfTheWeek--;
        Date thisDaysDate = getThisDaysDate(thisDayOfTheWeek, dayOfWeek.getValue(), todayDate);
        LocalDate finalDate  = thisDaysDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        taskAdapter = new TaskAdapter(getActivity());
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                Log.d("ShowTask", "found "+tasks.size()+" tasks from livedata");
                List<Task> thisDayTasks = new ArrayList<>();
                for (Task task: tasks){
                    switch (task.getTaskRecurrence()){
                        case DAILY:
                            thisDayTasks.add(task);
                            break;
                        case ONCE:
                            if (task.getDate().getDayOfWeek().getValue()==dayOfWeek.getValue() && finalDate.compareTo(task.getDate())==0){
                                thisDayTasks.add(task);
                            }
                            break;
                        case ONLY_ON:
                            if (task.getDayOfWeek().getValue() == dayOfWeek.getValue()){
                                thisDayTasks.add(task);
                            }
                    }

                }

                if (thisDayTasks.size()!=0){
                    mNoTasksTextview.setVisibility(View.INVISIBLE);
                }else {
                    mNoTasksTextview.setVisibility(View.VISIBLE);
                }
                taskAdapter.setTasks(thisDayTasks);
                mTasks = thisDayTasks;
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
            Log.d(TAG, "got "+tasks.size()+" to display for day "+dayOfWeek);
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
            holder.bindTask(ShowSundayTasksFragment.this.mTasks.get(position));
        }

        @Override
        public int getItemCount() {
            if (mTasks != null){
                return ShowSundayTasksFragment.this.mTasks.size();
            }else {
                return 0;
            }

        }
    }

    public  class TaskHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "TaskHolder";
        CardView taskCard;
        TextView mTaskTitle;
        TextView taskStartTime;
        TextView taskEndTime;
        ImageView alertTypeIcon;
        Task task;
        TextView recuranceTypeDisplayer;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                    Bundle args = new Bundle();
                    args.putString(EditTaskActivity.UUIDString, task.getTaskId().toString());
                    intent.putExtras(args);
                    startActivity(intent);
                }
            });
            mTaskTitle =(TextView) itemView.findViewById(R.id.task_title);
            //startEndTimeContainer =(TextView) itemView.findViewById(R.id.start_end_time);
            taskStartTime =(TextView) itemView.findViewById(R.id.task_start_time);
            taskEndTime = (TextView) itemView.findViewById(R.id.task_end_time);
            alertTypeIcon = (ImageView) itemView.findViewById(R.id.alert_type_icon);
            recuranceTypeDisplayer = (TextView) itemView.findViewById(R.id.recurance_displayer);

            taskCard = (CardView) itemView.findViewById(R.id.task_card);
        }

        void bindTask(Task task){
            this.task = task;
            Log.d(TAG, "received a task "+task);
            mTaskTitle.setText(task.getTaskTitle());
            recuranceTypeDisplayer.setText(getRecuranceText(task));
            taskStartTime.setText(task.getTaskStartTime().toString());
            taskEndTime.setText(task.getTaskEndTime().toString());
            alertTypeIcon.setImageDrawable(getActivity().getDrawable(getAlertTypeIcon(task)));
//            String startTime = task.getTaskStartTime().toString();
//            mStartEndTime.setText(startTime);

            //taskCard.setCardBackgroundColor(Utils.getColorResourceForTaskColor(task.getTaskColor()));

        }

        private int getAlertTypeIcon(Task task) {
            switch (task.getAlertType()){
                //case ALARM: return R.drawable.ic_baseline_access_alarm_24;
                case SILENT: return R.drawable.silence_icon;
                case NOTIFICATION: return R.drawable.notification_icon;
            }
            return 0;
        }

        private String getRecuranceText(Task task) {
            switch (task.getTaskRecurrence()){
                case ONLY_ON:return getWeekDayDisplayString(task);
                case DAILY: return "Everyday";
                case ONCE: return task.getDate().toString();
            }
            return null;
        }

        private String getWeekDayDisplayString(Task task) {
            switch (task.getDayOfWeek()){
                case FRIDAY: return "Fridays";
                case MONDAY: return "Mondays";
                case TUESDAY: return "Tuesdays";
                case WEDNESDAY:return "Wednesdays";
                case THURSDAY: return "Thursdays";
                case SUNDAY: return "Sundays";
                case SATURDAY: return "Saturdays";
            }
            return null;
        }
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

}
