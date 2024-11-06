package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private Long phoneNumber;
    private Bitmap profilePicture;
    private final boolean isAdmin;
    private Facility facility;
    private boolean receivesOrgAdmNotifications;

    // TODO constructor that gets info from database

    public User(String name, String email) throws Exception {
        this.setName(name); // it is important that name is set before profile picture
        this.setEmail(email);
        this.setPhoneNumber(null);
        this.setProfilePicture(null); // FIXME profile picture may not be provided by user and must then be auto-generated
        this.isAdmin = false; // TODO check DB
        this.setFacility(null);
        this.setReceivesOrgAdmNotifications(true);
    }

    public User(String name, String email, Long phoneNumber) throws Exception {
        this(name, email);
        this.setPhoneNumber(phoneNumber);
    }

    public User(String name, String email, Bitmap profilePicture) throws Exception {
        this(name, email);
        this.setProfilePicture(profilePicture);
    }

    public User(String name, String email, Long phoneNumber, Bitmap profilePicture) throws Exception {
        this(name, email, phoneNumber);
        this.setProfilePicture(profilePicture);
    }

    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("cannot set name to null");
        }
        if (name.isEmpty()) {
            throw new Exception("name cannot be empty");
        }
        this.name = name;
        // TODO update database
    }

    public void setEmail(String email) throws Exception {
        if (email == null) {
            throw new Exception("cannot set email to null");
        }
        if (email.isEmpty()) {
            throw new Exception("email cannot be empty");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new Exception("invalid email");
        }
        this.email = email;
        // TODO update database
    }

    public void setPhoneNumber(Long phoneNumber) throws Exception {
        if (phoneNumber != null) {
            if (phoneNumber <= 0) {
                throw new Exception("phone number cannot be a negative number");
            }
            if (phoneNumber.toString().length() > 15) {
                throw new Exception("invalid phone number, too many digits");
            }
            if (phoneNumber.toString().length() < 7) {
                throw new Exception("invalid phone number, not enough digits");
            }
        }
        // phone number of null is ok since it is optional,
        // null indicates the user has not defined their phone number
        this.phoneNumber = phoneNumber;
        // TODO update database
    }

    public void setProfilePicture(Bitmap profilePicture) {
        if (profilePicture == null) {
            // this is ok,
            // but it means we have to auto-generate a profile picture based on user's name
            // TODO generate profile picture based on user's name
        }
        this.profilePicture = profilePicture;
        // TODO update database
    }

    public void deleteProfilePicture() {
        this.setProfilePicture(null); // deletes and generates from user's name
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
        // TODO update database
    }

    public void setReceivesOrgAdmNotifications(boolean receivesOrgAdmNotifications){
        this.receivesOrgAdmNotifications = receivesOrgAdmNotifications;
        // TODO update database
    }

    public void deleteFacility() {
        this.setFacility(null);
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public Long getPhoneNumber(){
        return this.phoneNumber;
    }

    public Bitmap getProfilePicture(){
        return this.profilePicture;
    }

    public Facility getFacility(){
        return this.facility;
    }

    public boolean isAdmin(){
        return this.isAdmin;
    }

    public boolean isOrganizer(){
        return (this.facility != null); // user is an organizer if they have a facility
    }

    public boolean getReceivesOrgAdmNotifications() {
        return this.receivesOrgAdmNotifications;
    }

    public void deleteuser(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        userRef.document(this.name).delete()
                .addOnSuccessListener(onSuccessListener)
                .addonFailureListnere(onFailureListener);
    }

    public static void fetchUsers(OnSuccessListener<ArrayList<User>> onSuccessListener,OnFailureListener onFailureListener){
        userRef.get().addOnSuccessListener((queryDocumentSnapshots -> {
            ArrayList<User> users=new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                String name = doc.getString("name");
                String email = doc.getString("email");
                Long phoneNumber -doc.getLong("phoneNumber");
                User user = new User(Name, email, phoneNumber);
                users.add(user);
            }
            onSuccessListener.onSuccess(users);
        }).addOnFailureListener(onFailureListener);
    }
}
