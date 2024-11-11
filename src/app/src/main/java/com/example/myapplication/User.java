package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentReference;

import java.util.UUID;

/*
This class is creates a user object. It checks user info as well as the roll of the user.
It also tracks if the user has created a facility or if they have any events.
Additional functionalities including managing notification preferences and storage of information
provided by the user
 */
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

    /**
     * Simplest constructor for the User class
     * This constructor **creates a new user** and adds it to the database
     * Profile picture will automatically be generated based on user's name
     * User will not be admin by default
     * everything else is set to null
     * @param name
     * @param email
     * @throws Exception
     */
    public User(String name, String email) throws Exception {
        this.uniqueID = UUID.randomUUID().toString();
        this.setName(name); // it is important that name is set before profile picture
        this.setEmail(email);
        this.setPhoneNumber(null);
        this.setProfilePicture(null);
        this.isAdmin = false;
        this.setFacility(null);
        this.setReceivesOrgAdmNotifications(true);
        this.userRef = new DatabaseManager().createUser(this);
    }

    /**
     * This constructor is the same as the simplest one, but allows defining a phone number as well
     * @param name
     * @param email
     * @param phoneNumber
     * @throws Exception
     */
    public User(String name, String email, Long phoneNumber) throws Exception {
        this(name, email);
        this.setPhoneNumber(phoneNumber);
        this.updateDatabase();
    }

    /**
     * This constructor is the same as the simplest one, but allows defining a profile picture as well
     * @param name
     * @param email
     * @param profilePicture
     * @throws Exception
     */
    public User(String name, String email, Bitmap profilePicture) throws Exception {
        this(name, email);
        this.setProfilePicture(profilePicture);
        this.updateDatabase();
    }

    /**
     * This constructor essentially a combination of:
     * - the one that allows a phone number and
     * - the one that allows a profile picture
     * @param name
     * @param email
     * @param phoneNumber
     * @param profilePicture
     * @throws Exception
     */
    public User(String name, String email, Long phoneNumber, Bitmap profilePicture) throws Exception {
        this(name, email, phoneNumber);
        this.setProfilePicture(profilePicture);
        this.updateDatabase();
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

    private void updateDatabase() {
        new DatabaseManager().updateUser(this);
    }

    /**
     * Get this user's unique device ID
     * @return
     */
    public String getUniqueID() {
        return this.uniqueID;
    }

    /**
     * Set this user's name, throws exception on null or empty name
     * @param name
     * @throws Exception
     */
    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("cannot set name to null");
        }
        if (name.isEmpty()) {
            throw new Exception("name cannot be empty");
        }
        this.name = name;
        this.updateDatabase();
    }

    /**
     * Set this user's email, throws exception or null, empty or invalid email
     * @param email
     * @throws Exception
     */
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
        this.updateDatabase();
    }

    /**
     * Set this user's phone number, throws exception on invalid phone number
     * @param phoneNumber
     * @throws Exception
     */
    public void setPhoneNumber(Long phoneNumber) throws Exception {
        if (phoneNumber != null) { // the following checks only apply to non-null phone numbers
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
        this.updateDatabase();
    }

    /**
     * set this user's profile picture, generates a profile picture if it's null
     * @param profilePicture
     */
    public void setProfilePicture(Bitmap profilePicture) {
        if (profilePicture == null) {
            // this is ok,
            // but it means we have to auto-generate a profile picture based on user's name
            // TODO generate profile picture based on user's name
        }
        this.profilePicture = profilePicture;
        this.updateDatabase();
    }

    /**
     * delete this user's profile picture (resets it to the automatically generated one)
     */
    public void deleteProfilePicture() {
        this.setProfilePicture(null); // deletes and generates from user's name
    }

    /**
     * set this user's facility (they are now an organizer)
     * @param facility
     */
    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    /**
     * set this user's notification preference
     * @param receivesOrgAdmNotifications
     */
    public void setReceivesOrgAdmNotifications(boolean receivesOrgAdmNotifications){
        this.receivesOrgAdmNotifications = receivesOrgAdmNotifications;
        this.updateDatabase();
    }

    /**
     * delete this user's facility (user is no longer an organizer)
     */
    public void deleteFacility() {
        this.setFacility(null);
        this.updateDatabase();
    }

    /**
     * get this user's name
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * get this user's email
     * @return
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * get this user's phone number
     * @return
     */
    public Long getPhoneNumber(){
        return this.phoneNumber;
    }

    /**
     * get this user's profile picture
     * @return
     */
    public Bitmap getProfilePicture(){
        return this.profilePicture;
    }

    /**
     * get this user's facility (null iff user is not an organizer)
     * @return
     */
    public Facility getFacility(){
        return this.facility;
    }

    /**
     * get whether this user is an admin or not
     * @return
     */
    public boolean isAdmin(){
        return this.isAdmin;
    }

    /**
     * get whether this user is an organizer or not
     * @return
     */
    public boolean isOrganizer(){
        return (this.facility != null); // user is an organizer if they have a facility
    }

    /**
     * get the user's notification preference
     * @return
     */
    public boolean getReceivesOrgAdmNotifications() {
        return this.receivesOrgAdmNotifications;
    }

    /**
     * get the DocumentReference to the user in the database
     * @return
     */
    public DocumentReference getUserReference() {
        return this.userRef;
    }
}
