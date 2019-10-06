package com.example.taskmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.database.TaskCursorWrapper;
import com.example.taskmanager.model.database.TaskDataBaseSchema;
import com.example.taskmanager.model.database.TaskOpenHelper;

import static com.example.taskmanager.model.database.TaskDataBaseSchema.TaskTable.*;
import static com.example.taskmanager.model.database.TaskDataBaseSchema.TaskTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TasksRepository {

    private static TasksRepository instance;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    private TasksRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskOpenHelper(context).getWritableDatabase();

    }

    public static TasksRepository getInstance(Context context) {
        if (instance == null)
            instance = new TasksRepository(context);

        return instance;
    }

    public List<Task> getTasksList(int numCurentPage, String username) {

        List<Task> mTaskListTodo = new ArrayList<>();
        List<Task> mTaskListDoing = new ArrayList<>();
        List<Task> mTaskListDone = new ArrayList<>();

        TaskCursorWrapper cursor = (TaskCursorWrapper) queryTasks(
                null,
                Cols.USERNAME + " = ?",
                new String[]{username});
        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                switch (cursor.getTask().getState()) {
                    case TODO:
                        mTaskListTodo.add(cursor.getTask());
                        break;
                    case DOING:
                        mTaskListDoing.add(cursor.getTask());
                        break;
                    case DONE:
                        mTaskListDone.add(cursor.getTask());
                        break;

                }
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        switch (numCurentPage) {
            case 0:
                return mTaskListTodo;
            case 1:
                return mTaskListDoing;
            case 2:
                return mTaskListDone;

        }
        return null;
    }


    //read
    public Task getTask(UUID id) {
        TaskCursorWrapper cursor = (TaskCursorWrapper) queryTasks(
                null,
                Cols.UUID + " = ?",
                new String[]{id.toString()});

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return null;

            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public Task getDateTask(String str, String username) {
        TaskCursorWrapper cursor = (TaskCursorWrapper) queryTasks(
                null,
                Cols.USERNAME + " = ?",
                new String[]{username});

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return null;

            SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormatDate.format(cursor.getTask().getDate());

            SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
            String time = dateFormatTime.format(cursor.getTask().getDate());

            if (date.equals(str) || time.equals(str))
                return cursor.getTask();

            return null;
        } finally {
            cursor.close();
        }
    }

    public Task searchTask(String str, String username) {
        TaskCursorWrapper cursor = (TaskCursorWrapper) queryGetTasks(str, username);

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return null;

            return cursor.getTask();
        } finally {
            cursor.close();
        }

    }

    public CursorWrapper queryGetTasks(String str, String username) {

        String selectQuery = "SELECT * FROM " + TaskTable.NAME + " WHERE " +
                Cols.USERNAME + "=\"" + username + "\" AND " +
                Cols.TITLE + "=\"" + str + "\" OR " + Cols.DESCRIPTION + "=\"" + str + "\" OR " +
                Cols.TIME + "=\"" + str + "\" OR " + Cols.DATE + "=\"" + str + "\"";

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);

        return new TaskCursorWrapper(cursor);
    }


    public void addTasks(Task task) {
        ContentValues values = getContentValues(task);
        mDatabase.insert(NAME, null, values);
    }

    public void updateTask(Task task) {
        ContentValues values = getContentValues(task);
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getUUID().toString()};
        mDatabase.update(NAME, values, where, whereArgs);
    }

    //delete
    public void deleteTask(Task task) {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getUUID().toString()};
        mDatabase.delete(NAME, where, whereArgs);
    }

    //delete
    public void deleteTask(UUID id) {
        deleteTask(getTask(id));
    }

    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, task.getUUID().toString());
        values.put(Cols.TITLE, task.getTaskTitle());
        values.put(Cols.DESCRIPTION, task.getDescription());
        values.put(Cols.DATE, task.getDate().getTime());
        values.put(Cols.TIME, task.getTime().getTime());
        values.put(Cols.STATE, task.getState().name());
        values.put(Cols.USERNAME, task.getUsername());

        return values;
    }

    private CursorWrapper queryTasks(String[] columns, String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(NAME,
                columns,
                where,
                whereArgs,
                null,
                null,
                null);

        return new TaskCursorWrapper(cursor);
    }

    public void deleteAll() {
        mDatabase.delete(NAME, null, null);

    }

}