package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DatabaseManager { // static class
    private final FirebaseFirestore db;

    public DatabaseManager() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void createUser(User user) {
        String userID = user.getUniqueID();
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("isAdmin", false);
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("profilePicture", user.getProfilePicture());
        userData.put("receivesOrgAdmNotifications", user.getReceivesOrgAdmNotifications());
        // FIXME insert facility? how?
        DocumentReference userRef = this.db.collection("users").document(userID);
        userRef.set(userData);
        CollectionReference facilityCol = userRef.collection("facility");
    }

    public void updateUser(User user) {
        String userID = user.getUniqueID();
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("profilePicture", user.getProfilePicture());
        userData.put("receivesOrgAdmNotifications", user.getReceivesOrgAdmNotifications());
        // FIXME update facility? how?
        DocumentReference userRef = this.db.collection("users").document(userID);
        userRef.update(userData);
    }

    public User getUser(String userID) {
        DocumentReference userRef = this.db.collection("users").document(userID);
        CollectionReference facilityCol = userRef.collection("facility");
        DocumentReference facilityRef = facilityCol.document(); // FIXME import facility from database if there is one?
        final User[] user = new User[1];

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<String, Object> userData = (HashMap<String, Object>) documentSnapshot.getData();
                if (userData == null) {
                    throw new UserDoesNotExist("this user does not exist in the database");
                }

                // get user data from document

                Object isAdminTemp = userData.get("isAdmin");
                if (isAdminTemp == null) {
                    throw new UserDoesNotExist("this user was missing the isAdmin field");
                }
                boolean isAdmin = (boolean) isAdminTemp;

                Object nameTemp = userData.get("name");
                if (nameTemp == null) {
                    throw new UserDoesNotExist("this user was missing the name field");
                }
                String name = (String) nameTemp;

                Object emailTemp = userData.get("email");
                if (emailTemp == null) {
                    throw new UserDoesNotExist("this user was missing the email field");
                }
                String email = (String) emailTemp;

                Object phoneNumberTemp = userData.get("phoneNumber");
                if (phoneNumberTemp == null) {
                    throw new UserDoesNotExist("this user was missing the phoneNumber field");
                }
                Long phoneNumber = (Long) phoneNumberTemp;

                Object profilePictureTemp = userData.get("profilePicture");
                if (profilePictureTemp == null) {
                    throw new UserDoesNotExist("this user was missing the profilePicture field");
                }
                Bitmap profilePicture = (Bitmap) profilePictureTemp;

                Object notificationTemp = userData.get("receivesOrgAdmNotifications");
                if (notificationTemp == null) {
                    throw new UserDoesNotExist("this user was missing the receivesOrgAdmNotifications field");
                }
                boolean receivesOrgAdmNotifications = (boolean) notificationTemp;

                try {
                    user[0] = new User(name, email, phoneNumber, profilePicture);
                    // FIXME pass isAdmin?
                    // FIXME pass notification preference?
                } catch (Exception e) {
                    user[0] = null;
                    throw new RuntimeException(e);
                }
            }
        });

        return user[0];
    }
}
