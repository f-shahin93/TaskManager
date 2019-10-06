package com.example.taskmanager.model.database;

public class TaskDataBaseSchema {

    public static final String NAME = "task.db";

    public static final class TaskTable {
        public static final String NAME = "Task";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String STATE = "state";
            public static final String USERNAME = "username";
        }
    }

    public static final class UserTable {

        public static final String NAME = "User";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String UUID = "uuid";
            public static final String USERNAME = "userName";
            public static final String PASSWORD = "password";
        }
    }

}




