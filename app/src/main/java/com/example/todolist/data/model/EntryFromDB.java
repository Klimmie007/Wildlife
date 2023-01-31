package com.example.todolist.data.model;

import com.google.gson.annotations.SerializedName;

public class EntryFromDB {
    @SerializedName("_id")
    public String id;
    public String UserID, ImageID, SpeciesID;
    public double Longitude, Latitude;
    public EntryFromDB(String UserID, String ImageID, String SpeciesID, double Longitude, double Latitude)
    {
        this.ImageID = ImageID;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.UserID = UserID;
        this.SpeciesID = SpeciesID;
    }
}
