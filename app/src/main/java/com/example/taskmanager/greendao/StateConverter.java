package com.example.taskmanager.greendao;

import com.example.taskmanager.model.TasksState;

import org.greenrobot.greendao.converter.PropertyConverter;

public class StateConverter implements PropertyConverter<TasksState,String> {
    @Override
    public TasksState convertToEntityProperty(String databaseValue) {
        if(databaseValue == null)
            return null;

        return TasksState.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(TasksState entityProperty) {
        if (entityProperty == null)
            return null;

        return entityProperty.name();

    }
}
