package com.example.taskmanager.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.model.User;

import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String stringUUID = getString(getColumnIndex(TaskDataBaseSchema.UserTable.Cols.UUID));
        String username = getString(getColumnIndex(TaskDataBaseSchema.UserTable.Cols.USERNAME));
        String password = getString(getColumnIndex(TaskDataBaseSchema.UserTable.Cols.PASSWORD));

        UUID uuid = UUID.fromString(stringUUID);

        User user = new User(uuid);
        user.setUserName(username);
        user.setPassword(password);

        return user;
    }

}
