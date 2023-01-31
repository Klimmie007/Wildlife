package com.example.todolist.data.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

public class Entry {
    public String entryID;
    public User user;
    public Bitmap image;
    public String imageID;
    public double Latitude;
    public double Longitude;
    public SpeciesTag species;
    public Entry(){}
    public Entry(String entryID, String userID, String imageID, String speciesID, double Latitude, double Longitude)
    {
        this.Longitude = Longitude;
        this.species = new SpeciesTag();
        species._id = speciesID;
        this.Latitude = Latitude;
        this.user = new User();
        this.user._id = userID;
        this.imageID = imageID;
        this.entryID = entryID;
    }
}
