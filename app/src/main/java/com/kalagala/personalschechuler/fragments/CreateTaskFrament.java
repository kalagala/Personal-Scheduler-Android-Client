package com.kalagala.personalschechuler.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.activities.ShowTasksActivity;
import com.kalagala.personalschechuler.model.AlertType;
import com.kalagala.personalschechuler.model.TaskRecurrence;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskColor;
import com.kalagala.personalschechuler.model.database.AppPersistentData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class CreateTaskFrament extends Fragment{
    public static final String FRAGMENT_PURPOSE="Fragment_Purpose";
    public static final int ARG_EDIT_TASK = 1;
    public static final int ARG_CREATE_TASK =2;
    Task task;

    TextView taskTitleDisplay;
    EditText taskTitle;
    EditText taskStartTime;
    EditText taskEndTime;
    EditText taskDate;
    RadioGroup taskColorChooserContainer;
    Spinner repeatType;
    Spinner alertType;
    Spinner dayOfWeek;
    LinearLayout datePickerContainer;
    LinearLayout dayOfTheWeekPickerContainer;
    Button addTaskButton;


    public static Fragment newInstance(){
        return new CreateTaskFrament();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        Log.d("create fragment", "oncreate view called");


        view =inflater.inflate(R.layout.fragment_create_task, container, false);

        task = new Task();

        taskTitleDisplay = (TextView) view.findViewById(R.id.task_title_view);
        taskTitle =(EditText) view.findViewById(R.id.task_title_editor);
        taskStartTime = (EditText) view.findViewById(R.id.task_start_time);
        taskEndTime = (EditText) view.findViewById(R.id.task_end_time);
        taskDate = (EditText) view.findViewById(R.id.date_picker);
        taskColorChooserContainer = (RadioGroup) view.findViewById(R.id.task_color_chooser_container);
        repeatType =(Spinner) view.findViewById(R.id.repeat_mode_picker);
        alertType = (Spinner) view.findViewById(R.id.alert_mode_picker);
        dayOfWeek = (Spinner) view.findViewById(R.id.day_of_week_picker);
        datePickerContainer = (LinearLayout)  view.findViewById(R.id.date_picker_container);
        addTaskButton = (Button) view.findViewById(R.id.add_task_button);
        dayOfTheWeekPickerContainer = (LinearLayout) view
                .findViewById(R.id.day_of_the_week_picker_container);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addTask()){
                    Intent intent = new Intent(getActivity(), ShowTasksActivity.class);
                    startActivity(intent);

                    Toast toast = Toast.makeText(getActivity()
                            , R.string.task_create_successfully, Toast.LENGTH_SHORT);
                    toast.show();
                    getActivity().finish();
                }
            }
        });

        setAppropriateInputFields();

        taskTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                task.setTaskTitle(charSequence.toString());
                taskTitleDisplay.setText(task.getTaskTitle());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        taskStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = (selectedHour<10 )?
                                "0"+ new Integer(selectedHour).toString() : new Integer(selectedHour).toString();
                        String minutes = (selectedMinute<10) ?
                                "0"+ new Integer(selectedMinute).toString(): new Integer(selectedMinute).toString();
                        taskStartTime.setText(
                                hour + ":" + minutes);
                        task.setTaskStartTime(LocalTime.of(selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        taskEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = (selectedHour<10 )?
                                "0"+ new Integer(selectedHour).toString() : new Integer(selectedHour).toString();
                        String minutes = (selectedMinute<10) ?
                                "0"+ new Integer(selectedMinute).toString(): new Integer(selectedMinute).toString();
                        taskEndTime.setText(
                                hour + ":" + minutes);
                        task.setTaskEndTime(LocalTime.of(selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {

                        selectedMonth = selectedMonth + 1;
                        taskDate.setText("" + selectedDay + "/" + selectedMonth + "/" + selectedYear);
                        task.setDate(LocalDate.of(selectedYear, selectedMonth, selectedDay));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();

            }
        });

        taskColorChooserContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton checkedRadioButton= radioGroup.findViewById(i);

                task.setTaskColor(
                                TaskColor.getTaskColorWithId(
                                        radioGroup.indexOfChild(checkedRadioButton)));
                setAppropriateInputFields();
            }
        });

        ArrayAdapter<CharSequence> repeatTypeAdapter = ArrayAdapter
                .createFromResource(
                        getActivity(),
                        R.array.repeat_mode,
                        R.layout.support_simple_spinner_dropdown_item);
        repeatType.setAdapter(repeatTypeAdapter);
        Log.d("Create", "setting repeate type to "+task.getTaskRecurrence().getId());
        repeatType.setSelection(task.getTaskRecurrence().getId());
        repeatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                task.setTaskRecurrence(TaskRecurrence.getRecuranceById(position));
                setAppropriateInputFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> alertTypeAdapter = ArrayAdapter
                .createFromResource(
                        getActivity(),
                        R.array.alert_mode,
                        R.layout.support_simple_spinner_dropdown_item);
        alertType.setAdapter(alertTypeAdapter);
        alertType.setSelection(task.getAlertType().getId());
        alertType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                task.setAlertType(AlertType.getAlertById(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> dayOfWeekPickerAdapter = ArrayAdapter
                .createFromResource(
                        getActivity(),
                        R.array.days_of_a_week,
                        R.layout.support_simple_spinner_dropdown_item
                        );
        dayOfWeek.setAdapter(dayOfWeekPickerAdapter);
        dayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    task.setDayOfWeek(DayOfWeek.of(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;

    }

    private boolean addTask() {
        if (dataIsValid()){
            class InsertData extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    AppPersistentData db = Room.databaseBuilder(getActivity(),
                            AppPersistentData.class, "task").build();

                    db.taskDao().insertTask(task);
                    return null;
                }
            }
            Log.d("CreateTaskFragment", "data inserted successfully");

            return true;

        }

        return false;
    }

    private boolean dataIsValid() {
        if(checkTaskTitle() && checKTaskStartTime() && checkTaskEndTime()){
            if (task.getTaskRecurrence() == TaskRecurrence.ONCE){
                return checkTaskDate();
            }
            return true;
        }else {
            return false;
        }
    }

    private boolean checkTaskDate() {
        if (task.getDate() == null){
            showInfoDialog(R.string.empty_date, R.string.empty_date_message);
            return false;
        }else {
            //TODO check if date is available

        }
        return true;
    }

    private boolean checkTaskEndTime() {
        //TODO  check if time is available
        if (task.getTaskEndTime() == null){
            showInfoDialog(R.string.set_task_end_time, R.string.no_end_time_message);
            return false;
        }else if (task.getTaskEndTime().compareTo(task.getTaskStartTime()) == 0){
            showInfoDialog(R.string.invalid_time, R.string.same_end_time_message);
            return false;
        }else if (task.getTaskEndTime().compareTo(task.getTaskStartTime()) < 0){
            showInfoDialog(R.string.invalid_time, R.string.task_end_time_before_start);
            return false;
        }
        return true;
    }

    private boolean checKTaskStartTime() {
        if (task.getTaskStartTime() == null){
            showInfoDialog(R.string.set_task_start_time, R.string.no_start_time_message);
            return false;
        }
        return true;
    }

    private boolean checkTaskTitle() {
        if (task.getTaskTitle().equals("") || task.getTaskTitle().equals("New Task")){
            showInfoDialog(R.string.set_task_title, R.string.no_task_title_message);
            return false;
        }else {
            return true;
        }
    }

    void showInfoDialog(int titleStrinResource, int messageStringResource){
        new AlertDialog.Builder(getActivity())
                .setTitle(titleStrinResource)
                .setMessage(messageStringResource)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void setAppropriateInputFields() {
        ((RadioButton) taskColorChooserContainer.getChildAt(task.getTaskColor().getColorId())).setChecked(true);
        switch (task.getTaskRecurrence()){
            case ONCE:

                Log.d("CreateTask", "task is set to once");
                if (dayOfTheWeekPickerContainer.getVisibility()==View.VISIBLE){
                    dayOfTheWeekPickerContainer.setVisibility(View.INVISIBLE);
                }
                if (datePickerContainer.getVisibility() == View.INVISIBLE){
                    datePickerContainer.setVisibility(View.VISIBLE);
                }
                break;
            case DAILY:
                Log.d("CreateTask", "task is set to daily");
                if (dayOfTheWeekPickerContainer.getVisibility()== View.VISIBLE){
                    dayOfTheWeekPickerContainer.setVisibility(View.INVISIBLE);
                }
                if (datePickerContainer.getVisibility() == View.VISIBLE){
                    datePickerContainer.setVisibility(View.INVISIBLE);
                }
                break;
            case ONLY_ON:
                Log.d("CreateTask", "task is set only on");
                if (datePickerContainer.getVisibility() == View.VISIBLE){
                    datePickerContainer.setVisibility(View.INVISIBLE);
                }
                if (dayOfTheWeekPickerContainer.getVisibility() == View.INVISIBLE){
                    dayOfTheWeekPickerContainer.setVisibility(View.VISIBLE);
                }
                break;

        }
    }
}
