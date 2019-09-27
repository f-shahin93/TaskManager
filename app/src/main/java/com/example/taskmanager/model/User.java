package com.example.taskmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String mUserName;
    private String mPassword;
    private UUID mId;

    public User(String userName, String password) {
        this.mUserName = userName;
        this.mPassword = password;
        //this.mId = UUID.randomUUID();
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public UUID getUUID() {
        return mId;
    }
}