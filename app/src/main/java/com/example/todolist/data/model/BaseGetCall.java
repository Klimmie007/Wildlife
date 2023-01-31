package com.example.todolist.data.model;

public class BaseGetCall<T> extends BaseCall{

    public T filter;
    public BaseGetCall(String collect, T filtre)
    {
        super(collect);
        this.filter = filtre;
    }
}
