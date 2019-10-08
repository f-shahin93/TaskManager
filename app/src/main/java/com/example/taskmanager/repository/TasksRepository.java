package com.example.taskmanager.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.greendao.TaskOpenHelper;
import com.example.taskmanager.model.DaoMaster;
import com.example.taskmanager.model.DaoSession;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TasksRepository {

    private static TasksRepository instance;
    private Context mContext;
    private TaskDao mTaskDao;

    private TasksRepository(Context context) {

        mContext = context.getApplicationContext();

        SQLiteDatabase db = new TaskOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mTaskDao = daoSession.getTaskDao();

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

        List<Task> tasks = mTaskDao.queryBuilder()
                .where(TaskDao.Properties.Username.eq(username))
                .list();

        for (int i = 0; i < tasks.size(); i++) {

            switch (tasks.get(i).getState()) {
                case TODO:
                    mTaskListTodo.add(tasks.get(i));
                    break;
                case DOING:
                    mTaskListDoing.add(tasks.get(i));
                    break;
                case DONE:
                    mTaskListDone.add(tasks.get(i));
                    break;
            }
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
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MUUID.eq(id))
                .unique();
    }

    public Task getDateTask(String str, String username) {

        List<Task> tasks = mTaskDao.queryBuilder()
                .where(TaskDao.Properties.Username.eq(username))
                .list();

        SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");

        for (int i = 0; i < tasks.size(); i++) {

            String date = dateFormatDate.format(tasks.get(i).getDate());
            String time = dateFormatTime.format(tasks.get(i).getTime());

            if (date.equals(str) || time.equals(str))
                return tasks.get(i);
        }
        return null;

    }

    public Task searchTask(String str, String username) {
        QueryBuilder<Task> qb = mTaskDao.queryBuilder();
        qb.where(qb.and(TaskDao.Properties.Username.eq(username),
                qb.or(TaskDao.Properties.MTaskTitle.eq(str), TaskDao.Properties.MDescription.eq(str))));

        return qb.unique();

    }

    //insert
    public void addTasks(Task task) {
        mTaskDao.insert(task);
    }

    //update
    public void updateTask(Task task) {
        mTaskDao.update(task);
    }

    //delete task
    public void deleteTask(Task task) {
        mTaskDao.delete(task);
    }

    //delete by uuid
    public void deleteTask(UUID id) {
        deleteTask(getTask(id));
    }

    //delete All
    public void deleteAll() {
        mTaskDao.deleteAll();
    }

}