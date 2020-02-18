package com.example.taskmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.taskmanager.greendao.StateConverter;
import com.example.taskmanager.greendao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity (nameInDb = "Task")
public class Task implements Parcelable {

    @Id (autoincrement = true)
    private Long id;

    @Property(nameInDb = "uuid")
    @Index(unique = true)
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID mUUID;

    @Property(nameInDb = "title")
    private String mTaskTitle;

    @Property(nameInDb = "description")
    private String mDescription;

    @Property(nameInDb = "date")
    private Date mDate;

    @Property(nameInDb = "time")
    private Date mTime;

    @Property(nameInDb = "state")
    @Convert(converter = StateConverter.class , columnType = String.class)
    private TasksState mState;

    @Property(nameInDb = "username")
    private String username;

    @Property (nameInDb = "photopath")
    private String photoPath;


    public Task() {
        mUUID = UUID.randomUUID();
        mDate = new Date();
    }

    public Task(UUID uuid) {
        mUUID = uuid;
        mDate = new Date();
    }

    public Task(String taskTitle) {
        mTaskTitle = taskTitle;

        Random random = new Random();
        int num = random.nextInt(3);
        if (num == 0) {
            setState(TasksState.TODO);
        } else if (num == 1) {
            setState(TasksState.DOING);
        } else if (num == 2) {
            setState(TasksState.DONE);
        }
    }

    @Generated(hash = 247413777)
    public Task(Long id, UUID mUUID, String mTaskTitle, String mDescription,
            Date mDate, Date mTime, TasksState mState, String username,
            String photoPath) {
        this.id = id;
        this.mUUID = mUUID;
        this.mTaskTitle = mTaskTitle;
        this.mDescription = mDescription;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mState = mState;
        this.username = username;
        this.photoPath = photoPath;
    }

    protected Task(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        mTaskTitle = in.readString();
        mDescription = in.readString();
        username = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public String getTaskTitle() {
        return mTaskTitle;
    }

    public void setTaskTitle(String taskTitle) {

        this.mTaskTitle = taskTitle;
    }

    public TasksState getState() {
        return mState;
    }

    public void setState(TasksState state) {

        mState = state;
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

    public String getMTaskTitle() {
        return this.mTaskTitle;
    }

    public void setMTaskTitle(String mTaskTitle) {
        this.mTaskTitle = mTaskTitle;
    }

    public String getMDescription() {
        return this.mDescription;
    }

    public void setMDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getMDate() {
        return this.mDate;
    }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }

    public Date getMTime() {
        return this.mTime;
    }

    public void setMTime(Date mTime) {
        this.mTime = mTime;
    }

    public TasksState getMState() {
        return this.mState;
    }

    public void setMState(TasksState mState) {
        this.mState = mState;
    }

    public String getPhotoName() {
        return "IMG_" + mUUID + ".jpg";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(mTaskTitle);
        parcel.writeString(mDescription);
        parcel.writeString(username);
    }

}