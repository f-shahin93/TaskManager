package com.example.taskmanager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;

public class Task implements Serializable {
    private UUID mUUID;
    private String mTaskTitle;
    private String mDescription;
    private Date mDate;
    private Date mTime;
    private TasksState mState;
    private String username;

    public Task() {
        mUUID = UUID.randomUUID();
        mDate = new Date();
    }

    public Task(UUID uuid) {
        mUUID = uuid;
        mDate = new Date();
    }

    public Task(String taskTitle) {
        mTaskTitle = taskTitle;

        Random random = new Random();
        int num = random.nextInt(3);
        if (num == 0) {
            setState(TasksState.TODO);
        } else if (num == 1) {
            setState(TasksState.DOING);
        } else if (num == 2) {
            setState(TasksState.DONE);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public String getTaskTitle() {
        return mTaskTitle;
    }

    public void setTaskTitle(String taskTitle) {

        this.mTaskTitle = taskTitle;
    }

    public TasksState getState() {
        return mState;
    }

    public void setState(TasksState state) {

        mState = state;
    }

    /*@Override
    public String toString() {
        System.out.println();
        return "Tasks{" +
                "mTaskTitle='" + mTaskTitle + '\'' +
                ", mState=" + mState +
                '}';
    }*/
}