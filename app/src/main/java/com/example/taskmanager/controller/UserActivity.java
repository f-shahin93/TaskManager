package com.example.taskmanager.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_container_UserList);
        if (fragment == null)
            fragmentManager.beginTransaction()
                    .add(R.id.activity_container_UserList, UserFragment.newInstance())
                    .commit();


    }


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserActivity.class);
        return intent;
    }

}
