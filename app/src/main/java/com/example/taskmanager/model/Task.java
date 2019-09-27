package com.example.taskmanager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;

public class Task implements Serializable {
    private UUID mID;
    private String mTaskTitle;
    private String mDescription;
    private Date mDate;
    private Date mTime;
    private TasksState mState;

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

    public UUID getID() {
        return mID;
    }

    public Task() {
        mID = UUID.randomUUID();
        mDate =generateRandomDate();
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

    private Date generateRandomDate() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(2000, 2019);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.getTime();
    }

    private int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    @Override
    public String toString() {
        System.out.println();
        return "Tasks{" +
                "mTaskTitle='" + mTaskTitle + '\'' +
                ", mState=" + mState +
                '}';
    }
}