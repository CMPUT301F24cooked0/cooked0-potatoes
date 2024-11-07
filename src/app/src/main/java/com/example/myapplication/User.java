package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class User {
    private String name;
    private String email;
    private Long phoneNumber;
    private Bitmap profilePicture;
    private final boolean isAdmin;
    private Facility facility;
    private boolean receivesOrgAdmNotifications;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private CollectionReference facilityCol;
    private HashMap<String, Object> userData;

    // TODO constructor that gets info from database

    public User(String name, String email) throws Exception {
        db = FirebaseFirestore.getInstance();
        this.userRef = db.collection("users").document(); // create new user
        this.facilityCol = this.userRef.collection("facility");
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
        // update database after setting name
        userData.put("name", this.name);
        this.userRef.update(userData);
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
        userData.put("email", this.email);
        this.userRef.update(userData); // update database after setting email
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
        userData.put("phoneNumber", this.phoneNumber);
        this.userRef.update(userData); // update database after setting phone number
    }

    public void setProfilePicture(Bitmap profilePicture) {
        if (profilePicture == null) {
            // this is ok,
            // but it means we have to auto-generate a profile picture based on user's name
            // TODO generate profile picture based on user's name
        }
        this.profilePicture = profilePicture;
        userData.put("profilePicture", this.profilePicture);
        this.userRef.update(userData); // update database after setting profile picture
    }

    public void deleteProfilePicture() {
        this.setProfilePicture(null); // deletes and generates from user's name
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public void setReceivesOrgAdmNotifications(boolean receivesOrgAdmNotifications){
        this.receivesOrgAdmNotifications = receivesOrgAdmNotifications;
        userData.put("receivesOrgAdmNotifications", this.receivesOrgAdmNotifications);
        this.userRef.update(userData); // update database after setting receivesOrgAdmNotifications
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

    public DocumentReference getUserRef() {
        return this.userRef; // return reference to user in database
    }
}
