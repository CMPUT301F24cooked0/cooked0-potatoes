package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import com.google.firebase.firestore.DocumentReference;

// import java.util.UUID;

// UUID.randomUUID().toString(); -- a potential way to get a unique ID (untested)

/*
This class is creates a user object. It checks user info as well as the roll of the user.
It also tracks if the user has created a facility or if they have any events.
Additional functionalities including managing notification preferences and storage of information
provided by the user
 */
public class User {
    private static final FirebaseFirestore db=FirebaseFirestore.getInstance();
    protected static final CollectionReference userRef=db.collection("Users");
    private String uniqueID;
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
    public User(String uniqueID, String name, String email) throws Exception {
        this.uniqueID = uniqueID;
        this.setName(name); // it is important that name is set before profile picture
        this.setEmail(email);
        this.setPhoneNumber(null);
        this.setProfilePicture(null);
        this.isAdmin = false;
        this.setFacility(null);
        this.setReceivesOrgAdmNotifications(true);
    }

    /**
     * This constructor is the same as the simplest one, but allows defining a phone number as well
     * @param name
     * @param email
     * @param phoneNumber
     * @throws Exception
     */
    public User(String uniqueID, String name, String email, Long phoneNumber) throws Exception {
        this(uniqueID, name, email);
        this.setPhoneNumber(phoneNumber);
    }

    /**
     * This constructor is the same as the simplest one, but allows defining a profile picture as well
     * @param name
     * @param email
     * @param profilePicture
     * @throws Exception
     */
    public User(String uniqueID, String name, String email, Bitmap profilePicture) throws Exception {
        this(uniqueID, name, email);
        this.setProfilePicture(profilePicture);
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
    public User(String uniqueID, String name, String email, Long phoneNumber, Bitmap profilePicture) throws Exception {
        this(uniqueID, name, email, phoneNumber);
        this.setProfilePicture(profilePicture);
    }

    /***
     * Method to delete user from database and is called in AdministratorBrowseUsersFragment when admin confirms the deletion of the user.
     * @param onSuccessListener
     * @param onFailureListener
     */
    public void deleteUser(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        userRef.document(this.name).delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
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
    public User(String uniqueID, String name, String email, Long phoneNumber, Bitmap profilePicture, boolean isAdmin, boolean receivesOrgAdmNotifications, DocumentReference userRef, Facility facility) throws Exception {
        this.uniqueID = uniqueID;
        this.setName(name);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setProfilePicture(profilePicture);
        this.isAdmin = isAdmin;
        this.setReceivesOrgAdmNotifications(receivesOrgAdmNotifications);
        this.userRef = userRef;
        this.facility = facility;
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
    }

    /**
     * delete this user's facility (user is no longer an organizer)
     */
    public void deleteFacility() {
        this.setFacility(null);
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

    /***
     * Method that gets users data from the database and to be called in the AdministratorBrowseUsersFragment to populate the list.
     * @param onSuccessListener
     * @param onFailureListener
     * @deprecated please DO NOT use this method. Use the DatabaseManager instead
     */
    public static void fetchUsers(OnSuccessListener<ArrayList<User>> onSuccessListener,OnFailureListener onFailureListener){
        userRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<User> users=new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                String name = doc.getString("name");
                String email = doc.getString("email");
                Long phoneNumber =doc.getLong("phoneNumber");

                try {
                    User user=new User(name,email,phoneNumber);
                    users.add(user);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            onSuccessListener.onSuccess(users);
        }).addOnFailureListener(onFailureListener);
    }

    public void setUserReference(DocumentReference userRef) {
        this.userRef = userRef;
    }

    /**
     * get the DocumentReference to the user in the database
     * @return
     */
    public DocumentReference getUserReference() {
        return this.userRef;
    }
}
