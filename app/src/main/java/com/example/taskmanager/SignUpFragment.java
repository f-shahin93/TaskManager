package com.example.taskmanager;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends DialogFragment {

    public static final String EXTRA_USERNAME_SignUp = "EXTRA_USERNAME_SignUp";
    public static final String EXTRA_PASSWORD_SignUp = "EXTRA_PASSWORD_SignUp";
    public static final String ARG_PARAM1 = "ARG_PARAM1";
    public static final String ARG_PARAM2 = "ARG_PARAM2";
    private EditText mETUserSignUp;
    private EditText mETPasswordSignUp;
    private Button mButtonSignUp;


    public static SignUpFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        SignUpFragment fragment = new SignUpFragment();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_signup, null, false);

        mButtonSignUp = view.findViewById(R.id.button_SignUP_SignUp);
        mETUserSignUp = view.findViewById(R.id.etSignUP_userName);
        mETPasswordSignUp = view.findViewById(R.id.etSignUP_password);

        mETUserSignUp.setText(getArguments().getString(ARG_PARAM1));
        mETPasswordSignUp.setText(getArguments().getString(ARG_PARAM2));

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mETUserSignUp.getText().toString().equals("") || mETPasswordSignUp.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_USERNAME_SignUp, mETUserSignUp.getText().toString());
                    intent.putExtra(EXTRA_PASSWORD_SignUp, mETPasswordSignUp.getText().toString());

                    Fragment fragment = getTargetFragment();
                    fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                }
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.button_signUp)
                .setView(view).create();
    }
}


/*

package com.example.taskmanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpFragment extends Fragment {

    public static final String EXTRA_USERNAME_SignUp = "EXTRA_USERNAME_SignUp";
    public static final String EXTRA_PASSWORD_SignUp = "EXTRA_PASSWORD_SignUp";
    public static final String ARG_PARAM1 = "ARG_PARAM1";
    public static final String ARG_PARAM2 = "ARG_PARAM2";
    private EditText mETUserSignUp;
    private EditText mETPasswordSignUp;
    private Button mButtonSignUp;


    public static SignUpFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        SignUpFragment fragment = new SignUpFragment();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_signup, container, false);
        mButtonSignUp = view.findViewById(R.id.button_SignUP_SignUp);
        mETUserSignUp = view.findViewById(R.id.etSignUP_userName);
        mETPasswordSignUp = view.findViewById(R.id.etSignUP_password);

        mETUserSignUp.setText(getArguments().getString(ARG_PARAM1));
        mETPasswordSignUp.setText(getArguments().getString(ARG_PARAM2));

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mETUserSignUp.getText().toString().equals("") || mETPasswordSignUp.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_USERNAME_SignUp, mETUserSignUp.getText().toString());
                    intent.putExtra(EXTRA_PASSWORD_SignUp, mETPasswordSignUp.getText().toString());

                    Fragment fragment = getTargetFragment();
                    fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    fragment.getActivity().getSupportFragmentManager().popBackStackImmediate();



                   /* FragmentManager fragmentManager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(SignUpFragment.this)
                            .commit();*/
/*                }
            }
        });

        return view;
    }
}*/
