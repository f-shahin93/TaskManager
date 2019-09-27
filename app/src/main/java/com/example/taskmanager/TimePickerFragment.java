package com.example.taskmanager;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimePickerFragment extends DialogFragment {

    public static final String ARG_TIME_PICKER = "ARG time picker";
    public static final String EXTRA_Task_TIME = "com.example.taskmanager.taskTime";
    public static final String AM_PM = "com.example.taskmanager.task_am_pm";

    private TimePicker mTimePicker;
    private Date mTime;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(Date date) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME_PICKER, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTime = (Date) getArguments().getSerializable(ARG_TIME_PICKER);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time_picker, null, false);
        mTimePicker = view.findViewById(R.id.time_picker);

        initTimePicker();

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar calendar = Calendar.getInstance();
                        int hour = mTimePicker.getCurrentHour();
                        int min = mTimePicker.getCurrentMinute();
                        settime(hour, min);

                    }
                }).setView(view).create();

    }
    private void settime(int hourOfDay, int minute) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";
        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        String strMin = (datetime.get(Calendar.MINUTE) < 9) ? "0" + datetime.get(Calendar.MINUTE) : Integer.toString(datetime.get(Calendar.MINUTE));

        Date time =  mTime;
        time.setHours(Integer.parseInt(strHrsToShow));
        time.setMinutes(Integer.parseInt(strMin));

        Intent intent = new Intent();
        intent.putExtra(EXTRA_Task_TIME, time);
        intent.putExtra(AM_PM,am_pm);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }


    public void initTimePicker() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTime);

        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

    }
}