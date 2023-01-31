package com.example.todolist.data.model;

public class BaseCall {
    public String dataSource = "Wildlife", database="Wildlife", collection;
    public BaseCall(String collection)
    {
        this.collection = collection;
    }
}
