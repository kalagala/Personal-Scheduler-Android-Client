package com.kalagala.personalschechuler.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.lifecycle.ViewModelProviders;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.activities.ShowTasksActivity;
import com.kalagala.personalschechuler.model.AlertType;
import com.kalagala.personalschechuler.model.TaskRecurrence;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskColor;
import com.kalagala.personalschechuler.database.TaskRepository;
import com.kalagala.personalschechuler.viewmodel.TaskViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class CreateTaskFrament extends Fragment{
    public static final String FRAGMENT_PURPOSE="Fragment_Purpose";
    public static final int ARG_EDIT_TASK = 1;
    public static final int ARG_CREATE_TASK =2;
    private static final String TAG ="CreateTaskFragment";
    Task task;

    private TextView taskTitleDisplayTextView;
    private EditText taskTitleEditText;
    private EditText taskStartTimeEditText;
    private EditText taskEndTimeEditText;
    private EditText taskDateEditText;
    private RadioGroup taskColorChooserContainer;
    private Spinner repeatTypeSpinner;
    private Spinner alertTypeSpinner;
    private Spinner dayOfWeekSpinner;
    private LinearLayout datePickerContainerLinearLayout;
    private LinearLayout dayOfTheWeekPickerContainerLinearLayout;
    private Button addTaskButton;

    private TaskViewModel taskViewModel;


    public static Fragment newInstance(){
        return new CreateTaskFrament();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        Log.d("create fragment", "oncreate view called");


        view =inflater.inflate(R.layout.fragment_create_task, container, false);

        task = new Task();

        taskTitleDisplayTextView = (TextView) view.findViewById(R.id.task_title_view);
        taskTitleEditText =(EditText) view.findViewById(R.id.task_title_editor);
        taskStartTimeEditText = (EditText) view.findViewById(R.id.task_start_time);
        taskEndTimeEditText = (EditText) view.findViewById(R.id.task_end_time);
        taskDateEditText = (EditText) view.findViewById(R.id.date_picker);
        taskColorChooserContainer = (RadioGroup) view.findViewById(R.id.task_color_chooser_container);
        repeatTypeSpinner =(Spinner) view.findViewById(R.id.repeat_mode_picker);
        alertTypeSpinner = (Spinner) view.findViewById(R.id.alert_mode_picker);
        dayOfWeekSpinner = (Spinner) view.findViewById(R.id.day_of_week_picker);
        datePickerContainerLinearLayout = (LinearLayout)  view.findViewById(R.id.date_picker_container);
        addTaskButton = (Button) view.findViewById(R.id.add_task_button);
        dayOfTheWeekPickerContainerLinearLayout = (LinearLayout) view
                .findViewById(R.id.day_of_the_week_picker_container);

        setAppropriateInputFields();

        //listeners
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
        taskTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                task.setTaskTitle(charSequence.toString());
                taskTitleDisplayTextView.setText(task.getTaskTitle());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        taskStartTimeEditText.setOnClickListener(new View.OnClickListener() {
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
                        taskStartTimeEditText.setText(
                                hour + ":" + minutes);
                        task.setTaskStartTime(LocalTime.of(selectedHour, selectedMinute));
                        Log.d(TAG, "task Start Time has been Set to "+task.getTaskStartTime().toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        taskEndTimeEditText.setOnClickListener(new View.OnClickListener() {
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
                        taskEndTimeEditText.setText(hour + ":" + minutes);
                        task.setTaskEndTime(LocalTime.of(selectedHour, selectedMinute));
                        Log.d(TAG, "task endtime has been set to "+task.getTaskEndTime().toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        taskDateEditText.setOnClickListener(new View.OnClickListener() {
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
                        taskDateEditText.setText("" + selectedDay + "/" + selectedMonth + "/" + selectedYear);
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
        repeatTypeSpinner.setAdapter(repeatTypeAdapter);
        repeatTypeSpinner.setSelection(task.getTaskRecurrence().getId());
        repeatTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        alertTypeSpinner.setAdapter(alertTypeAdapter);
        alertTypeSpinner.setSelection(task.getAlertType().getId());
        alertTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        dayOfWeekSpinner.setAdapter(dayOfWeekPickerAdapter);
        dayOfWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            taskViewModel.insert(task);
            return true;
        }
        return false;
    }

    private boolean dataIsValid() {
        if(checkTaskTitle() && checKTaskStartTime() && checkTaskEndTime()&& checkAvailabilyOfTimeWindow()){
            if (task.getTaskRecurrence() == TaskRecurrence.ONCE){
                return checkTaskDate();
            }
            return true;
        }else {
            return false;
        }
    }

    private boolean checkAvailabilyOfTimeWindow() {
        List<Task> allTasks = (new TaskRepository(getActivity().getApplication()).getAllTasks()).getValue();
//        for (Task taskFromDb: allTasks){
//            switch (task.getTaskRecurrence()){
//                case ONLY_ON:
//                    //TODO figure out how to check time conflicts
//            }
//        }
        return true;
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

    private void showInfoDialog(int titleStrinResource, int messageStringResource){
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
                if (dayOfTheWeekPickerContainerLinearLayout.getVisibility()==View.VISIBLE){
                    dayOfTheWeekPickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                if (datePickerContainerLinearLayout.getVisibility() == View.INVISIBLE){
                    datePickerContainerLinearLayout.setVisibility(View.VISIBLE);
                }
                break;
            case DAILY:
                if (dayOfTheWeekPickerContainerLinearLayout.getVisibility()== View.VISIBLE){
                    dayOfTheWeekPickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                if (datePickerContainerLinearLayout.getVisibility() == View.VISIBLE){
                    datePickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case ONLY_ON:
                if (datePickerContainerLinearLayout.getVisibility() == View.VISIBLE){
                    datePickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                if (dayOfTheWeekPickerContainerLinearLayout.getVisibility() == View.INVISIBLE){
                    dayOfTheWeekPickerContainerLinearLayout.setVisibility(View.VISIBLE);
                }
                break;

        }
    }
}
