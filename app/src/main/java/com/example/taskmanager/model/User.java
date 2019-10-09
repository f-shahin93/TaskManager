package com.example.taskmanager.model;

import com.example.taskmanager.greendao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity (nameInDb = "User")
public class User {

    @Id (autoincrement = true)
    private Long id;

    @Property (nameInDb = "uuid")
    @Index (unique = true)
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID mUUID;

    @Property(nameInDb = "username")
    private String mUserName;

    @Property(nameInDb = "password")
    private String mPassword;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Property(nameInDb = "date")
    private Date mDate;


    public User(String userName, String password) {
        this.mUserName = userName;
        this.mPassword = password;
        this.mUUID = UUID.randomUUID();
        this.mDate = new Date();
    }

    public User(UUID uuid) {
        this.mUUID = uuid;
        this.mDate = new Date();
    }

    @Generated(hash = 1923706488)
    public User(Long id, UUID mUUID, String mUserName, String mPassword,
            Date mDate) {
        this.id = id;
        this.mUUID = mUUID;
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        this.mDate = mDate;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getMUUID() {
        return this.mUUID;
    }

    public void setMUUID(UUID mUUID) {
        this.mUUID = mUUID;
    }

    public String getMUserName() {
        return this.mUserName;
    }

    public void setMUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getMPassword() {
        return this.mPassword;
    }

    public void setMPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public Date getMDate() {
        return this.mDate;
    }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }
}