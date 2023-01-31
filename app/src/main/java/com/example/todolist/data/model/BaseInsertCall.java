package com.example.todolist.data.model;

public class BaseInsertCall<T> extends BaseCall{
    public T document;
    public BaseInsertCall(String collection, T document)
    {
        super(collection);
        this.document = document;
    }
}
