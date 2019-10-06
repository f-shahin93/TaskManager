package com.example.taskmanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
public class LoginFragment extends Fragment {

    public static final int REQUEST_CODE_SIGN_UP = 0;
    public static final String TAG_SIGN_UP = "tag signUp";
    private Button mButtonLogin;
    private Button mButtonSignUp;
    private EditText mETUserLogin;
    private EditText mETPasswordLogin;
    private String mUsername;
    private String mPass;
    private UserRepository mUserRepository;
    private boolean flagUsername = false;
    private boolean flagPassword = false;


    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mButtonLogin = view.findViewById(R.id.button_Login);
        mButtonSignUp = view.findViewById(R.id.button_Login_SignUp);
        mETUserLogin = view.findViewById(R.id.etLogin_userName);
        mETPasswordLogin = view.findViewById(R.id.etLogin_password);

        mUserRepository = UserRepository.getInstance(getContext());

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mETUserLogin.getText().toString().equals("") || mETPasswordLogin.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Cannot Login!", Toast.LENGTH_LONG).show();
                } else {

                    flagUsername = mUserRepository.searchUser(mETUserLogin.getText().toString());
                    if (flagUsername) {
                        flagPassword = mUserRepository.searchPassword(mETUserLogin.getText().toString(), mETPasswordLogin.getText().toString());
                        if (flagPassword) {
                            Toast.makeText(getActivity(), "You are logged in!", Toast.LENGTH_LONG).show();
                            Intent intent = TasksViewPagerActivity.newIntent(getContext(), mETUserLogin.getText().toString());

                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Incorrect Password!!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "this User not exist!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignUpFragment signUpFragment = SignUpFragment.newInstance(mETUserLogin.getText().toString(), mETPasswordLogin.getText().toString());
                signUpFragment.setTargetFragment(LoginFragment.this, REQUEST_CODE_SIGN_UP);
                signUpFragment.show(getFragmentManager(), TAG_SIGN_UP);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED || data == null)
            return;

        if (requestCode == REQUEST_CODE_SIGN_UP) {
            mUsername = data.getStringExtra(SignUpFragment.EXTRA_USERNAME_SignUp);
            mPass = data.getStringExtra(SignUpFragment.EXTRA_PASSWORD_SignUp);

            mETUserLogin.setText(data.getStringExtra(SignUpFragment.EXTRA_USERNAME_SignUp));
            mETPasswordLogin.setText(data.getStringExtra(SignUpFragment.EXTRA_PASSWORD_SignUp));

        }
    }

}