package com.example.taskmanager.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.greendao.TaskOpenHelper;
import com.example.taskmanager.model.DaoMaster;
import com.example.taskmanager.model.DaoSession;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository {
    private static UserRepository instance;
    private List<User> listUsername = new ArrayList<>();
    private Context mContext;
    private UserDao mUserDao;

    private UserRepository(Context context) {
        mContext = context.getApplicationContext();

        SQLiteDatabase db = new TaskOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mUserDao = daoSession.getUserDao();
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
        return mUserDao.queryBuilder()
                .where(UserDao.Properties.MUUID.eq(id))
                .unique();
    }

    //search by UUID
    public boolean searchUser(UUID id) {

        User user = mUserDao.queryBuilder()
                .where(UserDao.Properties.MUUID.eq(id))
                .unique();

        if(user == null)
            return false;
        else return true;
    }

    //search UserName by username
    public boolean searchUser(String username) {

        User user = mUserDao.queryBuilder()
                .where(UserDao.Properties.MUserName.eq(username))
                .unique();

        if(user == null)
            return false;
        else return true;
    }

    //search username and Password
    public boolean searchPassword(String username, String password) {
        QueryBuilder<User> qb = mUserDao.queryBuilder();
        qb.where(qb.and(UserDao.Properties.MUserName.eq(username),UserDao.Properties.MPassword.eq(password)));

        User user = qb.unique();

        if(user == null)
            return false;
        else return true;
    }


    public void addUser(User user) {
        mUserDao.insert(user);
    }

}