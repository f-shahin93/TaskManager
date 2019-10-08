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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TasksState;
import com.example.taskmanager.repository.TasksRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddInfoTaskFragment extends DialogFragment {


    public static final int REQUEST_CODE_DATE_PICKER = 1;
    public static final int REQUEST_CODE_TIME_PICKER = 2;
    public static final String TAG_DATE_PICKER = "Tag date picker";
    public static final String TAG_TIME_PICKER = "Tag time picker";
    public static final String EXTRA_TASK_ADD_INFO = "EXTRA task Add Info";
    public static final String ARG_NUM_CURENT_PAGE = "Arg mNumCurentPage";
    public static final String ARG_USERNAME = "ARG_USERNAME";
    public static final String BUNDLE_BUTTON_DATE = "bundle button date";
    public static final String BUNDLE_BUTTON_TIME = "bundle button time";

    private Spinner mSpinner;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mListStatusSpinnerItem;
    private TextView mTVTitle;
    private EditText mETTitleTask;
    private EditText mETDescriptionTask;
    private Button mButtonDatePicher;
    private Button mButtonTimePicher;
    Task mTask;
    String tempSpItem;
    private String am_pm;
    private int mNumCurentPage;
    private String mUsername;



    public static AddInfoTaskFragment newInstance(int numCurentPage ,String username) {
        Bundle args = new Bundle();
        AddInfoTaskFragment fragment = new AddInfoTaskFragment();
        args.putInt(ARG_NUM_CURENT_PAGE,numCurentPage);
        args.putString(ARG_USERNAME,username);
        fragment.setArguments(args);
        return fragment;
    }

    public AddInfoTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNumCurentPage = getArguments().getInt(ARG_NUM_CURENT_PAGE);
            mUsername = getArguments().getString(ARG_USERNAME);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_add_info_task, null, false);

        //mTVTitle = view.findViewById(R.id.tv_title);
        mETTitleTask = view.findViewById(R.id.et_addTitle);
        mETDescriptionTask = view.findViewById(R.id.et_addDescription);
        mButtonDatePicher = view.findViewById(R.id.button_addDatePicker);
        mButtonTimePicher = view.findViewById(R.id.button_addTimePicker);
        mSpinner = view.findViewById(R.id.spinner_addState);

        mTask = new Task();

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        mButtonTimePicher.setText(localDateFormat.format(mTask.getDate()));
        SimpleDateFormat localDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        mButtonDatePicher.setText(localDateFormat1.format(mTask.getDate()));

        if(savedInstanceState != null){
            mButtonTimePicher.setText(savedInstanceState.getString(BUNDLE_BUTTON_TIME));
            mButtonDatePicher.setText( savedInstanceState.getString(BUNDLE_BUTTON_DATE));
        }

        mTask.setTime(mTask.getDate());

        mListStatusSpinnerItem = new ArrayList<>();
        mListStatusSpinnerItem.add(String.valueOf(TasksState.TODO));
        mListStatusSpinnerItem.add(String.valueOf(TasksState.DOING));
        mListStatusSpinnerItem.add(String.valueOf(TasksState.DONE));

        mArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, mListStatusSpinnerItem);

        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mArrayAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                tempSpItem = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mButtonDatePicher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mTask.getDate());
                datePickerFragment.setTargetFragment(AddInfoTaskFragment.this, REQUEST_CODE_DATE_PICKER);
                datePickerFragment.show(getFragmentManager(), TAG_DATE_PICKER);
            }
        });

        mButtonTimePicher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mTask.getDate());
                timePickerFragment.setTargetFragment(AddInfoTaskFragment.this, REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getFragmentManager(), TAG_TIME_PICKER);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_new_task)
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(mETTitleTask.getText().toString().equals("") || mETDescriptionTask.getText().toString().equals("")){
                            Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_SHORT).show();
                        }else {
                            mTask.setTaskTitle(mETTitleTask.getText().toString());
                            mTask.setDescription(mETDescriptionTask.getText().toString());

                            if (tempSpItem.equals(TasksState.TODO.name())) {
                                mTask.setState(TasksState.TODO);
                            } else if (tempSpItem.equals(TasksState.DOING.name())) {
                                mTask.setState(TasksState.DOING);
                            } else if (tempSpItem.equals(TasksState.DONE.name())) {
                                mTask.setState(TasksState.DONE);
                            }
                            mTask.setUsername(mUsername);

                            TasksRepository.getInstance(getContext()).addTasks(mTask);

                            Intent intent = new Intent();
                            Fragment fragment = getTargetFragment();
                            fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .setView(view).create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_Task_DATE);
            mTask.setDate(date);
            SimpleDateFormat localDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
            mButtonDatePicher.setText(localDateFormat1.format(mTask.getDate()));
        }

        if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_Task_TIME);
            mTask.setTime(date);
            am_pm = data.getStringExtra(TimePickerFragment.AM_PM);
            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            mButtonTimePicher.setText(localDateFormat.format(mTask.getTime())+am_pm);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_BUTTON_TIME,mButtonTimePicher.getText().toString());
        outState.putString(BUNDLE_BUTTON_DATE,mButtonDatePicher.getText().toString());

    }
}