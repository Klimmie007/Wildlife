package com.example.todolist.data.model;

import com.google.gson.annotations.SerializedName;

public class UserContainer {
    @SerializedName("document")
    public User user;
    public String _id, username, password;


    public UserContainer(String id)
    {
        _id = id;
    }

    public UserContainer(String user, String pass)
    {
        username = user;
        password = pass;
    }

    public UserContainer(User newuser)
    {
        this.username = newuser.username;
    }
}
