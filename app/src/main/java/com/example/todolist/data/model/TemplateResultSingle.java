package com.example.todolist.data.model;

import com.google.gson.annotations.SerializedName;

public class TemplateResultSingle<T> {
    @SerializedName("document")
    public T value;
}
