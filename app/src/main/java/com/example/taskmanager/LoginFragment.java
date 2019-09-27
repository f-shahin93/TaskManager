package com.example.taskmanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;

import java.io.Serializable;
import java.util.HashMap;


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
    private String mUser;
    private String mPass;
    private UserRepository mUserRepository = UserRepository.getInstance();
    private boolean flag = false;

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

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mETUserLogin.getText().toString().equals("") || mETPasswordLogin.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Cannot Login!", Toast.LENGTH_LONG).show();
                } else {
                    /*if (mUser != null && mPass != null &&
                            mUser.equals(mETUserLogin.getText() + "") && mPass.equals(mETPasswordLogin.getText() + "")) */
                    User user = new User(mETUserLogin.getText().toString(), mETPasswordLogin.getText().toString());
                    flag = mUserRepository.searchUser(user);
                    if (flag) {
                        Toast.makeText(getActivity(), "You are logged in!", Toast.LENGTH_LONG).show();
                        Intent intent = TasksViewPagerActivity.newIntent(getActivity());
                        // intent.putExtra("Extra Login",user);
                        intent.putExtra("Extra Username", user.getUserName());
                        intent.putExtra("Extra password", user.getPassword());
                        startActivity(intent);
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

                /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_containerLogin, signUpFragment, "LoginFragTOSignFrag")
                        .addToBackStack("LoginFragTOSignFrag").commit();*/

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
            mUser = data.getStringExtra(SignUpFragment.EXTRA_USERNAME_SignUp);
            mPass = data.getStringExtra(SignUpFragment.EXTRA_PASSWORD_SignUp);

            mETUserLogin.setText(data.getStringExtra(SignUpFragment.EXTRA_USERNAME_SignUp));
            mETPasswordLogin.setText(data.getStringExtra(SignUpFragment.EXTRA_PASSWORD_SignUp));

            User user = new User(mUser, mPass);
            boolean flagAdd = mUserRepository.addUser(user);
            if (flagAdd){

            }else
                Toast.makeText(getActivity(), "this User is exist!", Toast.LENGTH_LONG).show();
        }
    }

}