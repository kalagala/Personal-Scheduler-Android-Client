package com.kalagala.personalschechuler.fragments;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import androidx.lifecycle.ViewModelProviders;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.activities.EditTaskActivity;
import com.kalagala.personalschechuler.database.AppPersistentData;
import com.kalagala.personalschechuler.model.AlertType;
import com.kalagala.personalschechuler.model.TaskDao;
import com.kalagala.personalschechuler.model.TaskRecurrence;
import com.kalagala.personalschechuler.model.Task;
import com.kalagala.personalschechuler.model.TaskColor;
import com.kalagala.personalschechuler.model.ValidationResponse;
import com.kalagala.personalschechuler.utils.NotificationHelpers;
import com.kalagala.personalschechuler.utils.ValidationAsync;
import com.kalagala.personalschechuler.utils.ValidationSync;
import com.kalagala.personalschechuler.viewmodel.TaskViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class EditTaskFragment extends Fragment {
    private static final String TAG = "EditTaskFragment";
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
    private Button deleteTaskButton;

    private TaskViewModel taskViewModel;


    public static Fragment newInstance() {
        return new EditTaskFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

    }

    class GetTask extends AsyncTask<Void, Void, Task> {
        String taskUUID;
        TaskDao taskDao;

        GetTask(Application application, String taskUUID) {
            AppPersistentData db = AppPersistentData.getDatabase(application);
            taskDao = db.taskDao();
            this.taskUUID = taskUUID;
        }

        @Override
        protected Task doInBackground(Void... voids) {
            Log.d(TAG, "fetching task from db");
            return taskDao.getTask(taskUUID);
        }

        @Override
        protected void onPostExecute(Task task) {
            super.onPostExecute(task);
            setTaskDetails(task);
        }
    }

    private void setTaskDetails(Task taskToEdit) {
        this.task = new Task(taskToEdit);
        setAppropriateInputFields();
        taskTitleDisplayTextView.setText(task.getTaskTitle());
        taskTitleEditText.setText(task.getTaskTitle());
        taskStartTimeEditText.setText(task.getTaskStartTime().toString());
        taskEndTimeEditText.setText(task.getTaskEndTime().toString());
        taskDateEditText.setText(task.getDate().toString());
        ((RadioButton) taskColorChooserContainer.getChildAt(task.getTaskColor().getColorId())).setChecked(true);
        repeatTypeSpinner.setSelection(task.getTaskRecurrence().getId());
        alertTypeSpinner.setSelection(task.getAlertType().getId());
        dayOfWeekSpinner.setSelection(task.getDayOfWeek().getValue() - 1);
        //TODO set attributes of a task;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        Log.d("create fragment", "oncreate view called");


        view = inflater.inflate(R.layout.fragment_edit_task, container, false);

        task = new Task();

        taskTitleDisplayTextView = (TextView) view.findViewById(R.id.edit_task_task_title_view);
        taskTitleEditText = (EditText) view.findViewById(R.id.edit_task_title_edit_text);
        taskStartTimeEditText = (EditText) view.findViewById(R.id.edit_task_task_start_time);
        taskEndTimeEditText = (EditText) view.findViewById(R.id.edit_task_task_end_time);
        taskDateEditText = (EditText) view.findViewById(R.id.date_picker);
        taskColorChooserContainer = (RadioGroup) view.findViewById(R.id.edit_task_task_color_chooser_container);
        repeatTypeSpinner = (Spinner) view.findViewById(R.id.edit_task_repeat_mode_picker);
        alertTypeSpinner = (Spinner) view.findViewById(R.id.edit_task_alert_mode_picker);
        dayOfWeekSpinner = (Spinner) view.findViewById(R.id.day_of_week_picker);
        datePickerContainerLinearLayout = (LinearLayout) view.findViewById(R.id.date_picker_container);
        addTaskButton = (Button) view.findViewById(R.id.edit_task_add_task_button);
        deleteTaskButton = (Button) view.findViewById(R.id.edit_task_delete_task_button);
        dayOfTheWeekPickerContainerLinearLayout = (LinearLayout) view
                .findViewById(R.id.day_of_the_week_picker_container);

        setAppropriateInputFields();

        //listeners
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete_title)
                        .setMessage(R.string.delete_task_message)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                taskViewModel.delete(task);
                                new NotificationHelpers(getActivity(), task).deleteNotification();
                                Toast toast = Toast.makeText(getActivity()
                                        , R.string.task_deleted_successfully, Toast.LENGTH_SHORT);
                                toast.show();
                                getActivity().finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                Log.d(TAG, "about to delete a task");
            }
        });
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAvailabilityOfAllInputFields()) {
                    //TODO do async validation and show a loading button
                    Log.d(TAG, "Waiting for db validations");
                    addTaskButton.setText(R.string.processing);
                    addTaskButton.setEnabled(false);
                    new GetDbTasks(getActivity().getApplication()).execute();
                }
//                if(addTask()){
//                    Intent intent = new Intent(getActivity(), ShowTasksActivity.class);
//                    startActivity(intent);
//
//                    Toast toast = Toast.makeText(getActivity()
//                            , R.string.task_create_successfully, Toast.LENGTH_SHORT);
//                    toast.show();
//                    getActivity().finish();
//                }
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
                        String hour = (selectedHour < 10) ?
                                "0" + new Integer(selectedHour).toString() : new Integer(selectedHour).toString();
                        String minutes = (selectedMinute < 10) ?
                                "0" + new Integer(selectedMinute).toString() : new Integer(selectedMinute).toString();
                        taskStartTimeEditText.setText(
                                hour + ":" + minutes);
                        task.setTaskStartTime(LocalTime.of(selectedHour, selectedMinute));
                        task.setTaskStartTimeHasBeenEnteredByUser(true);
                        Log.d(TAG, "task Start Time has been Set to " + task.getTaskStartTime().toString());
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
                        String hour = (selectedHour < 10) ?
                                "0" + new Integer(selectedHour).toString() : new Integer(selectedHour).toString();
                        String minutes = (selectedMinute < 10) ?
                                "0" + new Integer(selectedMinute).toString() : new Integer(selectedMinute).toString();
                        taskEndTimeEditText.setText(hour + ":" + minutes);
                        task.setTaskEndTime(LocalTime.of(selectedHour, selectedMinute));
                        task.setTaskEndTimeHasBeenEnteredByUser(true);
                        Log.d(TAG, "task endtime has been set to " + task.getTaskEndTime().toString());
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
                        task.setTaskDateHasBeenEnteredByUser(true);
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
                RadioButton checkedRadioButton = radioGroup.findViewById(i);

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
                task.setDayOfWeek(DayOfWeek.of(position + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Bundle args = getArguments();
        String uuidString = args.getString(EditTaskActivity.UUIDString);
        new GetTask(getActivity().getApplication(), uuidString).execute();
        return view;

    }


    private boolean validateAvailabilityOfAllInputFields() {
        ValidationSync validationSync = new ValidationSync(task);

        Log.d(TAG, "Validating task title availability");
        ValidationResponse taskTitleResponse =
                new ValidationResponse(validationSync.checkTaskTitle());
        if (!taskTitleResponse.isValid()) {
            showInfoDialog(
                    taskTitleResponse.getDialogTitleResourceId(),
                    taskTitleResponse.getDialogMessageResourceId()
            );
            return false;
        }

        ValidationResponse taskStartTimeResponse =
                new ValidationResponse(validationSync.checkTaskStartTime());
        Log.d(TAG, "validating task start time availability");
        if (!taskStartTimeResponse.isValid()) {
            showInfoDialog(
                    taskStartTimeResponse.getDialogTitleResourceId(),
                    taskStartTimeResponse.getDialogMessageResourceId()
            );
            return false;
        }

        Log.d(TAG, "validating task end time availability");
        ValidationResponse taskEndTimeResponse =
                new ValidationResponse(validationSync.checkTaskEndTime());
        if (!taskEndTimeResponse.isValid()) {
            showInfoDialog(
                    taskEndTimeResponse.getDialogTitleResourceId(),
                    taskEndTimeResponse.getDialogMessageResourceId()
            );
            return false;
        }


        if (task.getTaskRecurrence() == TaskRecurrence.ONCE) {
            Log.d(TAG, "Validating task date availability");
            ValidationResponse taskDateValidationResponse =
                    new ValidationResponse(validationSync.taskDateHasBeenEntered());
            if (!taskDateValidationResponse.isValid()) {
                showInfoDialog(
                        taskDateValidationResponse.getDialogTitleResourceId(),
                        taskDateValidationResponse.getDialogMessageResourceId()
                );
                return false;
            }
        }

//        if (dataIsAvailable()){
//            taskViewModel.insert(task);
//            return true;
//        }
        return true;


    }

    private void OnTaskFromDbLoaded(List<Task> tasksFromDb) {
        Log.d(TAG, "found " + tasksFromDb.size() + " tasks from db further processing needed");
        ValidationAsync validationAsync = new ValidationAsync(task);

        ValidationResponse validationResponse = validationAsync.validate(tasksFromDb);

        if (validationResponse.isValid()) {
            Log.d(TAG, "task has been verified against tasks from db and is now getting ed to db");
            taskViewModel.insert(task);
            new NotificationHelpers(getActivity(), task).deleteNotification();
            new NotificationHelpers(getActivity(), task).createNotification();
            Toast toast = Toast.makeText(getActivity()
                    , R.string.task_updated_successfully, Toast.LENGTH_SHORT);
            toast.show();
            getActivity().finish();
        } else {
            showInfoDialogWithCustomString(
                    validationResponse.getDialogTitleResourceId(),
                    validationResponse.getDialogMessageResourceId(),
                    validationResponse.getStringForPlaceHolder()
            );
            addTaskButton.setText(R.string.add_task);
            addTaskButton.setEnabled(true);
        }
    }

    private class GetDbTasks extends AsyncTask<Void, Void, List<Task>> {

        TaskDao taskDao;

        GetDbTasks(Application application) {
            AppPersistentData db = AppPersistentData.getDatabase(application);
            taskDao = db.taskDao();
        }

        @Override
        protected List<Task> doInBackground(Void... voids) {
            return taskDao.getAllTasksSync();
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            super.onPostExecute(tasks);
            OnTaskFromDbLoaded(tasks);
        }
    }

//    class getAllTasks extends AsyncTask<Void, Void, List<Task>> {
//        Fragment fragment;
//
//        getAllTasks(Fragment fragment){
//            this.fragment = fragment;
//        }
//
//        @Override
//        protected List<Task> doInBackground(Void... voids) {
//            return null;
//        }


//        @Override
//        protected ValidationResponse doInBackground(Task... tasks) {
//            ValidationAsync validationAsync = new ValidationAsync(tasks[0], fragment);
//            validationAsync.validate(new ArrayList<>());
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(ValidationResponse validationResponse) {
//            super.onPostExecute(validationResponse);
//        }
    //}

    //    private List<ValidationResponse> dataIsAvailable(ValidationSync validationSync) {
//        List<ValidationResponse> validationResponses = new ArrayList<>();
//
//        if (validationSync.checkTaskTitle().isValid()){
//            validationResponses.add(
//                    new ValidationResponse(validationSync.checkTaskTitle().isValid())
//            );
//        }else {
//            validationResponses.add(
//                    new ValidationResponse(validationSync.checkTaskTitle())
//            );
//        }
//        validationSync.checkTaskStartTime().isValid() && validationSync.checkTaskEndTime().isValid()){
//            if (task.getTaskRecurrence() == TaskRecurrence.ONCE){
//                validationSync.taskDateHasBeenEntered().isValid();
//            }
//            return true;
//        }else {
//            return false;
//        }
//    }
    private void setAppropriateInputFields() {
        if (taskColorChooserContainer != null) {
            ((RadioButton) taskColorChooserContainer.getChildAt(task.getTaskColor().getColorId())).setChecked(true);
        }

        switch (task.getTaskRecurrence()) {
            case ONCE:
                if (dayOfTheWeekPickerContainerLinearLayout.getVisibility() == View.VISIBLE) {
                    dayOfTheWeekPickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                if (datePickerContainerLinearLayout.getVisibility() == View.INVISIBLE) {
                    datePickerContainerLinearLayout.setVisibility(View.VISIBLE);
                }
                break;
            case DAILY:
                if (dayOfTheWeekPickerContainerLinearLayout.getVisibility() == View.VISIBLE) {
                    dayOfTheWeekPickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                if (datePickerContainerLinearLayout.getVisibility() == View.VISIBLE) {
                    datePickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case ONLY_ON:
                if (datePickerContainerLinearLayout.getVisibility() == View.VISIBLE) {
                    datePickerContainerLinearLayout.setVisibility(View.INVISIBLE);
                }
                if (dayOfTheWeekPickerContainerLinearLayout.getVisibility() == View.INVISIBLE) {
                    dayOfTheWeekPickerContainerLinearLayout.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    private void showInfoDialog(int titleStringResource, int messageStringResource) {
        new AlertDialog.Builder(getActivity())
                .setTitle(titleStringResource)
                .setMessage(messageStringResource)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void showInfoDialogWithCustomString(int titleStringResource, int messageStringResource, String taskTitle) {
        String msg = getActivity().getResources().getString(messageStringResource, taskTitle);
        new AlertDialog.Builder(getActivity())
                .setTitle(titleStringResource)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {

                })
                .show();
    }
}
