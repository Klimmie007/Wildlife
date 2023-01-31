package com.example.todolist.data.model;

import java.io.Serializable;

public class SpeciesTag implements Serializable {
    public String _id, species;
    public String toString()
    {
        return species;
    }
}
