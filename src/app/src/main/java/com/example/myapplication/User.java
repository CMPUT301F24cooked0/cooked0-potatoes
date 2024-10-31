package com.example.myapplication;

import android.content.Context;

public class User {
    private String name;
    private String email;
    private int phoneNumber;
    private boolean isAdmin;
    private Facility facility;
    private boolean notification;
    private EntrantStatus entrantStatus;

    public User(String name, String email, int phoneNumber, boolean isAdmin, Facility facility, EntrantStatus entrantStatus){
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.isAdmin=isAdmin;
        this.facility=facility;
        this.entrantStatus=entrantStatus;
        //firebase db
    }
}
