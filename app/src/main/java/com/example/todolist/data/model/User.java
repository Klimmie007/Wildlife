package com.example.todolist.data.model;

public class User {
    public String username, password, _id;
    public User(){}
    public User(LoggedInUser user)
    {
        _id = user.getUserId();
        username = user.getDisplayName();
    }
}
