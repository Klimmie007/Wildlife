package com.example.todolist.data.model;


import com.google.gson.annotations.SerializedName;

public class findById {
    @SerializedName("_id")
    public _id id;
    public findById(String id)
    {
        this.id =  new _id(id);
    }
}
