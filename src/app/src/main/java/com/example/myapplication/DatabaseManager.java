package com.example.myapplication;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
        DocumentReference userRef = this.db.collection("users").document(userID);
        userRef.set(userData);
        CollectionReference facilityCol = userRef.collection("facility");
    }
}
