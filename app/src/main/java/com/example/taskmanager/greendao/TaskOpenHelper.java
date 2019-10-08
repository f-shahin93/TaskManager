package com.example.taskmanager.greendao;

import android.content.Context;

import com.example.taskmanager.model.DaoMaster;

public class TaskOpenHelper extends DaoMaster.OpenHelper {

    public static final String NAME = "task.db";

    public TaskOpenHelper(Context context) {
        super(context, NAME);
    }
}
