package com.example.todolist.data.model;

import com.google.gson.annotations.SerializedName;

public class _id {
    @SerializedName("$oid")
    public String id;
    public _id(String id)
    {
        this.id = id;
    }
}
