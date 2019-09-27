package com.example.taskmanager.repository;



import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private HashMap<String ,String> mHMListUser = new HashMap<>();
    private List<User> listUsername = new ArrayList<>();


    private UserRepository() {
    }

    public static UserRepository getInstance(){
        instance = new UserRepository();
        return instance;
    }

    public List<User> getListUsername() {
        return listUsername;
    }

    public void setListUsername(List<User> listUsername) {
        this.listUsername = listUsername;
    }

    public boolean searchUser(User user) {
        Iterator<String> iterator = mHMListUser.keySet().iterator();
        while (iterator.hasNext()){
            if (iterator.next().equals(user.getUserName()))
                return true;
        }
        return false;
    }

    public boolean addUser(User user){
        User user1 = new User(user.getUserName() , user.getPassword());
        //listUsername.add(user1);
        Iterator<String> iterator = mHMListUser.keySet().iterator();
        while (iterator.hasNext()){
            if (iterator.next().equals(user.getUserName()))
                return false;
        }
        mHMListUser.put(user1.getUserName() , user1.getPassword());
        return true;
    }
}