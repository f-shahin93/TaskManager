package com.example.taskmanager.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TasksState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String stringUUID = getString(getColumnIndex(TaskDataBaseSchema.TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskDataBaseSchema.TaskTable.Cols.TITLE));
        String description = getString(getColumnIndex(TaskDataBaseSchema.TaskTable.Cols.DESCRIPTION));
        Long longDate = getLong(getColumnIndex(TaskDataBaseSchema.TaskTable.Cols.DATE));
        Long longTime = getLong(getColumnIndex(TaskDataBaseSchema.TaskTable.Cols.TIME));
        String state = getString(getColumnIndex(TaskDataBaseSchema.TaskTable.Cols.STATE));
        String username = getString(getColumnIndex(TaskDataBaseSchema.TaskTable.Cols.USERNAME));

        UUID uuid = UUID.fromString(stringUUID);

        Date date = new Date(longDate);
        Date time = new Date(longTime);

        Task task = new Task(uuid);
        task.setTaskTitle(title);
        task.setDescription(description);
        task.setDate(date);
        task.setTime(time);
        task.setUsername(username);

        if (state.equals(TasksState.TODO.name()))
            task.setState(TasksState.TODO);
        else if (state.equals(TasksState.DOING.name()))
            task.setState(TasksState.DOING);
        else if (state.equals(TasksState.DONE.name()))
            task.setState(TasksState.DONE);


        return task;
    }

}
