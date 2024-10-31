package com.example.myapplication;

public class User {
    private String name;
    private String email;
    private int phoneNumber;
    private String profilePicture;
    private boolean isAdmin;
    private boolean isOrganizer;
    private Facility facility;
    private boolean notification;

    public User(String name, String email, int phoneNumber, String profilePicture, boolean isAdmin, boolean isOrganizer, Facility facility){
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.profilePicture=profilePicture;
        this.isAdmin=isAdmin;
        this.isOrganizer=isOrganizer;
        this.facility=facility;
        //firebase db
    }

    public void setName(String name){
        this.name=name;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public void setProfilePicture(String profilePicture){
        this.profilePicture=profilePicture;
    }

    public void setNotification(boolean notification){
        this.notification=notification;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public int getPhoneNumber(){
        return phoneNumber;
    }

    public String getProfilePicture(){
        return profilePicture;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public boolean isOrganizer(){
        return isOrganizer;
    }

    public Facility facility(){
        return facility;
    }

    public boolean getNotification() {
        return notification;
    }

    public void deleteFacility(String facilityID){

    }



}
