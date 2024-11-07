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
    private final FirebaseFirestore db;
    private final DocumentReference userRef;
    private final CollectionReference facilityCol;
    private HashMap<String, Object> userData;

    public User(String uniqueID) throws RuntimeException {
        // this constructor generates the user from an existing user in the database based on deviceID
        this.uniqueID = uniqueID;
        this.db = FirebaseFirestore.getInstance();
        this.userRef = db.collection("users").document(this.getUniqueID());
        this.facilityCol = this.userRef.collection("facility"); // FIXME how to connect user to their facility, if any
        this.userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) throws RuntimeException {
                userData = (HashMap<String, Object>) documentSnapshot.getData();
                if (userData == null) {
                    throw new UserDoesNotExist("this user does not exist in the database");
                }

                // populate user data from document

                Object isAdminTemp = userData.get("isAdmin");
                if (isAdminTemp == null) {
                    throw new UserDoesNotExist("this user was missing the isAdmin field");
                }
                isAdmin = (boolean) isAdminTemp;

                Object nameTemp = userData.get("name");
                if (nameTemp == null) {
                    throw new UserDoesNotExist("this user was missing the name field");
                }
                try {
                    setName((String) nameTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Object emailTemp = userData.get("email");
                if (emailTemp == null) {
                    throw new UserDoesNotExist("this user was missing the email field");
                }
                try {
                    setEmail((String) emailTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Object phoneNumberTemp = userData.get("phoneNumber");
                if (phoneNumberTemp == null) {
                    throw new UserDoesNotExist("this user was missing the phoneNumber field");
                }
                try {
                    setPhoneNumber((Long) phoneNumberTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Object profilePictureTemp = userData.get("profilePicture");
                if (profilePictureTemp == null) {
                    throw new UserDoesNotExist("this user was missing the profilePicture field");
                }
                try {
                    setProfilePicture((Bitmap) profilePictureTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Object facilityTemp = userData.get("facility"); // FIXME
                if (facilityTemp == null) {
                    throw new UserDoesNotExist("this user was missing the facility field");
                }
                try {
                    setFacility((Facility) facilityTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Object notificationTemp = userData.get("receivesOrgAdmNotifications");
                if (notificationTemp == null) {
                    throw new UserDoesNotExist("this user was missing the receivesOrgAdmNotifications field");
                }
                try {
                    setReceivesOrgAdmNotifications((boolean) notificationTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public User(String name, String email) throws Exception {
        this.uniqueID = UUID.randomUUID().toString();
        this.userData = new HashMap<>();
        this.db = FirebaseFirestore.getInstance();
        this.userRef = db.collection("users").document(this.getUniqueID()); // create new user
        this.facilityCol = this.userRef.collection("facility");
        this.setName(name); // it is important that name is set before profile picture
        this.setEmail(email);
        this.setPhoneNumber(null);
        this.setProfilePicture(null); // FIXME profile picture may not be provided by user and must then be auto-generated
        this.isAdmin = false; // TODO insert into database
        userData.put("isAdmin", false);
        this.userRef.update(userData);
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
        this.facility = facility; // TODO make database aware of user's facility
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
