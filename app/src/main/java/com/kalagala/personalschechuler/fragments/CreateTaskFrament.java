package com.kalagala.personalschechuler.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.model.Task;

import java.util.Calendar;

public class CreateTaskFrament extends Fragment{
    Task mTask;

    EditText taskStartTime;
    EditText taskEndTime;
    EditText taskDate;
    Spinner repeatType;
    Spinner alertType;
    Spinner dayOfWeek;
    LinearLayout datePickerContainer;
    LinearLayout dayOfTheWeekPickerContainer;
    

    public static Fragment newInstance(){
        return new CreateTaskFrament();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_create_task, container, false);
        mTask = new Task();

        taskStartTime = (EditText) view.findViewById(R.id.task_start_time);
        taskEndTime = (EditText) view.findViewById(R.id.task_end_time);
        taskDate = (EditText) view.findViewById(R.id.date_picker);
        repeatType =(Spinner) view.findViewById(R.id.repeat_mode_picker);
        alertType = (Spinner) view.findViewById(R.id.alert_mode_picker);
        dayOfWeek = (Spinner) view.findViewById(R.id.day_of_week_picker);
        datePickerContainer = (LinearLayout)  view.findViewById(R.id.date_picker_container);
        dayOfTheWeekPickerContainer = (LinearLayout) view
                .findViewById(R.id.day_of_the_week_picker_container);


        switch (mTask.getTaskRecurrence()){
            case ONCE:
                dayOfTheWeekPickerContainer.setVisibility(View.INVISIBLE);
                break;
            case DAILY:
                dayOfTheWeekPickerContainer.setVisibility(View.INVISIBLE);
                datePickerContainer.setVisibility(View.INVISIBLE);
                break;
            case ONLY_ON:
                datePickerContainer.setVisibility(View.INVISIBLE);
                break;

        }
        taskStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = (selectedHour<10 )?
                                "0"+ new Integer(selectedHour).toString() : new Integer(selectedHour).toString();
                        String minutes = (selectedMinute<10) ?
                                "0"+ new Integer(selectedMinute).toString(): new Integer(selectedMinute).toString();
                        taskEndTime.setText(
                                hour + ":" + minutes);
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
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = (selectedHour<10 )?
                                "0"+ new Integer(selectedHour).toString() : new Integer(selectedHour).toString();
                        String minutes = (selectedMinute<10) ?
                                "0"+ new Integer(selectedMinute).toString(): new Integer(selectedMinute).toString();
                        taskEndTime.setText(
                                hour + ":" + minutes);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        selectedmonth = selectedmonth + 1;
                        taskDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();

            }
        });

        ArrayAdapter<CharSequence> repeatTypeAdapter = ArrayAdapter
                .createFromResource(
                        getActivity(),
                        R.array.repeat_mode,
                        R.layout.support_simple_spinner_dropdown_item);
        repeatType.setAdapter(repeatTypeAdapter);

        ArrayAdapter<CharSequence> alertTypeAdapter = ArrayAdapter
                .createFromResource(
                        getActivity(),
                        R.array.alert_mode,
                        R.layout.support_simple_spinner_dropdown_item);
        alertType.setAdapter(alertTypeAdapter);

        ArrayAdapter<CharSequence> dayOfWeekPickerAdapter = ArrayAdapter
                .createFromResource(
                        getActivity(),
                        R.array.days_of_a_week,
                        R.layout.support_simple_spinner_dropdown_item
                        );
        dayOfWeek.setAdapter(dayOfWeekPickerAdapter);

        return view;

    }
}
