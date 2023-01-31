package com.example.todolist.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TemplateResult<T> {
    @SerializedName("documents")
    public List<T> values;
}
