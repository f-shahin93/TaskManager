package com.example.taskmanager.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//import static com.example.taskmanager.model.database.TaskDataBaseSchema.TaskTable.NAME;
import static com.example.taskmanager.model.database.TaskDataBaseSchema.*;

public class TaskOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public TaskOpenHelper(@Nullable Context context) {

        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TaskTable.NAME +
                "(" +
                TaskTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskTable.Cols.UUID + ", " +
                TaskTable.Cols.TITLE + ", " +
                TaskTable.Cols.DESCRIPTION + ", " +
                TaskTable.Cols.DATE + ", " +
                TaskTable.Cols.TIME + ", " +
                TaskTable.Cols.STATE + ", " +
                TaskTable.Cols.USERNAME +
                " )"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + UserTable.NAME +
                "(" +
                UserTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserTable.Cols.UUID + ", " +
                UserTable.Cols.USERNAME + ", " +
                UserTable.Cols.PASSWORD +
                " )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
