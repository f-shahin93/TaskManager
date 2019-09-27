package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID ="Extra user id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_containerLogin);
        if (fragment == null)
            fragmentManager.beginTransaction()
                    .add(R.id.activity_containerLogin, LoginFragment.newInstance())
                    .commit();
    }

    public static Intent newIntent(Context context , UUID id) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        return intent;
    }

    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}
