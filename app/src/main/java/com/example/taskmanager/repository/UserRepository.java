package com.example.taskmanager.repository;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.database.TaskDataBaseSchema;
import com.example.taskmanager.model.database.TaskOpenHelper;
import com.example.taskmanager.model.database.UserCursorWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.example.taskmanager.model.database.TaskDataBaseSchema.UserTable.*;

public class UserRepository {
    private static UserRepository instance;
    private List<User> listUsername = new ArrayList<>();
    private Context mContext;
    private SQLiteDatabase mDatabase;


    private UserRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskOpenHelper(context).getWritableDatabase();
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null)
            instance = new UserRepository(context);
        return instance;
    }

    public List<User> getListUsername() {
        return listUsername;
    }

    public void setListUsername(List<User> listUsername) {
        this.listUsername = listUsername;
    }


    //read
    public User getUser(UUID id) {
        UserCursorWrapper cursor = (UserCursorWrapper) queryUsers(
                null,
                Cols.UUID + " = ?",
                new String[]{id.toString()});

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return null;

            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }

    //search by UUID
    public boolean searchUser(UUID id) {
        UserCursorWrapper cursor = (UserCursorWrapper) queryUsers(
                null,
                Cols.UUID + " = ?",
                new String[]{id.toString()});

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return false;

            cursor.getUser();
            return true;
        } finally {
            cursor.close();
        }
    }

    //search UserName
    public boolean searchUser(String username) {
        UserCursorWrapper cursor = (UserCursorWrapper) queryUsers(
                null,
                Cols.USERNAME + " = ?",
                new String[]{username});

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return false;

            return true;
        } finally {
            cursor.close();
        }
    }

    //search Password
    public boolean searchPassword(String username, String password) {
        UserCursorWrapper cursor = (UserCursorWrapper) queryUsers(
                null,
                Cols.USERNAME + " = ? AND " + Cols.PASSWORD + " = ?",
                new String[]{username, password});

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return false;

            return true;
        } finally {
            cursor.close();
        }
    }


    public void addUser(User user) {
        ContentValues values = getContentValues(user);
        mDatabase.insert(NAME, null, values);
    }

    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(TaskDataBaseSchema.UserTable.Cols.UUID, user.getUUID().toString());
        values.put(TaskDataBaseSchema.UserTable.Cols.USERNAME, user.getUserName());
        values.put(TaskDataBaseSchema.UserTable.Cols.PASSWORD, user.getPassword());

        return values;
    }

    private CursorWrapper queryUsers(String[] columns, String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(NAME,
                columns,
                where,
                whereArgs,
                null,
                null,
                null);

        return new UserCursorWrapper(cursor);
    }
}