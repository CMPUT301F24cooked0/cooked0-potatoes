package com.example.myapplication;

public class User {
    private String name;
    private String email;
    private Integer phoneNumber;
    private String profilePicture;
    private boolean isAdmin;
    private boolean isOrganizer;
    private Facility facility;
    private boolean notification;

    public User(String name, String email, Integer phoneNumber, String profilePicture, boolean isAdmin, boolean isOrganizer, Facility facility){
        // FIXME use setters here when possible to make use of validation code
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.profilePicture=profilePicture; // FIXME profile picture may not be provided by user and must then be auto-generated
        this.isAdmin=isAdmin;
        this.isOrganizer=isOrganizer;
        this.facility=facility;
        //firebase db
    }

    public void setName(String name){
        // TODO validate name and throw exception otherwise
        this.name=name;
    }

    public void setEmail(String email){
        // TODO validate email and throw exception otherwise
        this.email=email;
    }

    public void setPhoneNumber(Integer phoneNumber){
        // TODO validate phone number and throw exception otherwise
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

    public Integer getPhoneNumber(){
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
