package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TasksRepository {

    private static TasksRepository instance = new TasksRepository();
    private List<Task> mTaskListTodo = new ArrayList<>();
    private List<Task> mTaskListDoing = new ArrayList<>();
    private List<Task> mTaskListDone = new ArrayList<>();

    private TasksRepository() {

    }

    public List<Task> getTaskListDoing() {
        return mTaskListDoing;
    }

    public void setTaskListDoing(List<Task> taskListDoing) {
        mTaskListDoing = taskListDoing;
    }

    public List<Task> getTaskListDone() {
        return mTaskListDone;
    }

    public void setTaskListDone(List<Task> taskListDone) {
        mTaskListDone = taskListDone;
    }

    public List<Task> getTaskListTodo() {
        return mTaskListTodo;
    }

    public void setTaskListTodo(List<Task> taskListTodo) {
        this.mTaskListTodo = taskListTodo;
    }

    public static TasksRepository getInstance() {

        // instance = new TasksRepository();
        return instance;
    }

    public void addTasks(Task task) {

        switch (task.getState()) {
            case TODO:
                mTaskListTodo.add(task);
                break;
            case DOING:
                mTaskListDoing.add(task);
                break;
            case DONE:
                mTaskListDone.add(task);
                break;
        }
    }

    public Task searchTask(UUID id, int numCurrentPage) {
        List<Task> mTask;
        mTask = getList(numCurrentPage);

        for (Task task : mTask) {
            if (task.getID().equals(id)) {
                return task;
            }
        }
        return null;
    }

    public void update(Task task, int numCurrentPage) {
        List<Task> mTask;
        mTask = getList(numCurrentPage);

        for (int i = 0; i < mTask.size(); i++) {
            if (task.getID().equals(mTask.get(i).getID())) {
                mTask.set(i, task);
            }
        }
    }

    public void delete(Task task, int numCurrentPage) {
        List<Task> mTask;
        mTask = getList(numCurrentPage);

        for (int i = 0; i < mTask.size(); i++) {
            if (task.getID().equals(mTask.get(i).getID())) {
                mTask.remove(i);
            }
        }
    }

    public void deleteAll() {

        for (int i = 0; i < mTaskListTodo.size(); i++) {
            mTaskListTodo.remove(i);
        }
        for (int i = 0; i < mTaskListDoing.size(); i++) {
            mTaskListDoing.remove(i);
        }
        for (int i = 0; i < mTaskListDone.size(); i++) {
            mTaskListDone.remove(i);
        }
    }

    public List getList(int numCurrentPage) {

        switch (numCurrentPage) {
            case 0: {
                return mTaskListTodo;

            }
            case 1: {
                return mTaskListDoing;
            }
            case 2: {
                return mTaskListDone;
            }
        }
        return null;
    }

}



