package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class User {
    private final String uniqueID;
    private String name;
    private String email;
    private Long phoneNumber;
    private Bitmap profilePicture;
    private boolean isAdmin;
    private Facility facility;
    private boolean receivesOrgAdmNotifications;
    private DocumentReference userRef;

    public User(String name, String email) throws Exception {
        this.uniqueID = UUID.randomUUID().toString();
        this.setName(name); // it is important that name is set before profile picture
        this.setEmail(email);
        this.setPhoneNumber(null);
        this.setProfilePicture(null); // FIXME profile picture may not be provided by user and must then be auto-generated
        this.isAdmin = false;
        this.setFacility(null);
        this.setReceivesOrgAdmNotifications(true);
        this.userRef = new DatabaseManager().createUser(this);
    }

    public User(String name, String email, Long phoneNumber) throws Exception {
        this(name, email);
        this.setPhoneNumber(phoneNumber);
        new DatabaseManager().updateUser(this);
    }

    public User(String name, String email, Bitmap profilePicture) throws Exception {
        this(name, email);
        this.setProfilePicture(profilePicture);
        new DatabaseManager().updateUser(this);
    }

    public User(String name, String email, Long phoneNumber, Bitmap profilePicture) throws Exception {
        this(name, email, phoneNumber);
        this.setProfilePicture(profilePicture);
        new DatabaseManager().updateUser(this);
    }

    /**
     * only use this constructor in DatabaseManager to instantiate a user from the data in the database
     * @param name
     * @param email
     * @param phoneNumber
     * @param profilePicture
     * @param isAdmin
     * @param receivesOrgAdmNotifications
     */
    public User(String name, String email, Long phoneNumber, Bitmap profilePicture, boolean isAdmin, boolean receivesOrgAdmNotifications, DocumentReference userRef, Facility facility) throws Exception {
        this(name, email, phoneNumber, profilePicture);
        this.isAdmin = isAdmin;
        this.setReceivesOrgAdmNotifications(receivesOrgAdmNotifications);
        this.userRef = userRef;
        this.facility = facility;
    }

    public String getUniqueID() {
        return this.uniqueID;
    }

    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("cannot set name to null");
        }
        if (name.isEmpty()) {
            throw new Exception("name cannot be empty");
        }
        this.name = name;
        new DatabaseManager().updateUser(this);
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
        new DatabaseManager().updateUser(this);
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
        new DatabaseManager().updateUser(this);
    }

    public void setProfilePicture(Bitmap profilePicture) {
        if (profilePicture == null) {
            // this is ok,
            // but it means we have to auto-generate a profile picture based on user's name
            // TODO generate profile picture based on user's name
        }
        this.profilePicture = profilePicture;
        new DatabaseManager().updateUser(this);
    }

    public void deleteProfilePicture() {
        this.setProfilePicture(null); // deletes and generates from user's name
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public void setReceivesOrgAdmNotifications(boolean receivesOrgAdmNotifications){
        this.receivesOrgAdmNotifications = receivesOrgAdmNotifications;
        new DatabaseManager().updateUser(this);
    }

    public void deleteFacility() {
        this.setFacility(null);
        new DatabaseManager().updateUser(this);
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

    public DocumentReference getUserReference() {
        return this.userRef;
    }
}
