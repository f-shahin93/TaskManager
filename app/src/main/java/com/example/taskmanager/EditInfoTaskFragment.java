package com.example.taskmanager;


import android.app.Activity;
import android.app.Dialog;
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
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditInfoTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInfoTaskFragment extends DialogFragment {

    public static final int REQUEST_CODE_DATE_PICKER = 1;
    public static final int REQUEST_CODE_TIME_PICKER = 2;
    public static final String TAG_DATE_PICKER = "Tag date picker";
    public static final String TAG_TIME_PICKER = "Tag time picker";
    public static final String ARG_NUM_CURENT_PAGE = "Arg numcurentPage";
    public static final String ARG_ID_EDIT_TASK = "ARG_ID_EDIT_TASK";
    public static final String BUNDLE_EDIT_DATE_TASK = "bundle edit dateTask";
    public static final String EXTRA_EDIT = "Extra edit";
    public static final String BUNDLE_BUTTON_TIME = "BUNDLE_BUTTON_TIME";
    public static final String BUNDLE_BUTTON_DATE = "BUNDLE_BUTTON_DATE";

    private Spinner mSpinner;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mListStatusSpinnerItem;
    private TextView mTVTitle;
    private EditText mETTitleTask;
    private EditText mETDescriptionTask;
    private Button mButtonDatePicher;
    private Button mButtonTimePicher;
    private TextView mTvCancel;
    private TextView mTvDelete;
    private TextView mTvEdit;
    private Task mTask;
    private String tempSpItem;
    private int mNumCurrentPage;
    private UUID mId;
    private TasksState mTasksState;
    private String mUsername;


    public EditInfoTaskFragment() {
        // Required empty public constructor
    }

    public static EditInfoTaskFragment newInstance(int numcurrentPage, UUID id) {
        EditInfoTaskFragment fragment = new EditInfoTaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_CURENT_PAGE, numcurrentPage);
        args.putSerializable(ARG_ID_EDIT_TASK, id);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditInfoTaskFragment newInstance(UUID id) {
        EditInfoTaskFragment fragment = new EditInfoTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID_EDIT_TASK, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNumCurrentPage = getArguments().getInt(ARG_NUM_CURENT_PAGE);
            mId = (UUID) getArguments().getSerializable(ARG_ID_EDIT_TASK);
            mTask = TasksRepository.getInstance(getContext()).getTask(mId);
            mUsername = mTask.getUsername();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_edit_info_task, null, false);

        //mTVTitle = view.findViewById(R.id.tv_titleEdit);
        mETTitleTask = view.findViewById(R.id.et_editTitle);
        mETDescriptionTask = view.findViewById(R.id.et_editDescription);
        mButtonDatePicher = view.findViewById(R.id.button_editDatePicker);
        mButtonTimePicher = view.findViewById(R.id.button_editTimePicker);
        mSpinner = view.findViewById(R.id.spinner_editState);
        mTvDelete = view.findViewById(R.id.tv_delete_item);
        mTvEdit = view.findViewById(R.id.tv_edit_item);
        mTvCancel = view.findViewById(R.id.tv_save_item);

        mETTitleTask.setText(mTask.getTaskTitle());
        mETDescriptionTask.setText(mTask.getDescription());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        mButtonDatePicher.setText(simpleDateFormat.format(mTask.getDate()));

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        mButtonTimePicher.setText(simpleDateFormat1.format(mTask.getDate()));

        if(savedInstanceState != null){
            mButtonTimePicher.setText(savedInstanceState.getString(BUNDLE_BUTTON_TIME));
            mButtonDatePicher.setText( savedInstanceState.getString(BUNDLE_BUTTON_DATE));
        }

        mListStatusSpinnerItem = new ArrayList<>();
        mListStatusSpinnerItem.add(String.valueOf(TasksState.TODO));
        mListStatusSpinnerItem.add(String.valueOf(TasksState.DOING));
        mListStatusSpinnerItem.add(String.valueOf(TasksState.DONE));

        mArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, mListStatusSpinnerItem);

        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mArrayAdapter);

        switch (mTask.getState()) {
            case TODO:
                mSpinner.setSelection(0);
                break;
            case DOING:
                mSpinner.setSelection(1);
                break;
            case DONE:
                mSpinner.setSelection(2);
                break;
        }

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
                datePickerFragment.setTargetFragment(EditInfoTaskFragment.this, REQUEST_CODE_DATE_PICKER);
                datePickerFragment.show(getFragmentManager(), TAG_DATE_PICKER);
            }
        });

        mButtonTimePicher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mTask.getDate());
                timePickerFragment.setTargetFragment(EditInfoTaskFragment.this, REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getFragmentManager(), TAG_TIME_PICKER);
            }
        });

        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TasksRepository.getInstance(getContext()).deleteTask(mTask.getUUID());

                Intent intent = new Intent();
                Fragment fragment = getTargetFragment();
                fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                dismiss();
            }
        });

        mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mETTitleTask.getText().toString().equals("") || mETDescriptionTask.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_SHORT).show();
                }else {
                    mTask.setTaskTitle(mETTitleTask.getText().toString());
                    mTask.setDescription(mETDescriptionTask.getText().toString());
                    mTasksState = TasksState.valueOf(tempSpItem);
                    mTask.setState(mTasksState);
                    mTask.setUsername(mUsername);
                    TasksRepository.getInstance(getContext()).updateTask(mTask);

                    Intent intent = new Intent();
                    Fragment fragment = getTargetFragment();
                    fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                    /*TasksViewPagerActivity activity = (TasksViewPagerActivity) getActivity();
                    activity.updateAllFragment();*/

                    dismiss();
                }
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.edit_task)
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
            mTask.setDate(date);
            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            mButtonTimePicher.setText(localDateFormat.format(mTask.getDate()));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_BUTTON_TIME,mButtonTimePicher.getText().toString());
        outState.putString(BUNDLE_BUTTON_DATE,mButtonDatePicher.getText().toString());

    }

}