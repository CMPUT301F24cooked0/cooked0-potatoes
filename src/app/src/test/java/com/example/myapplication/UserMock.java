package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentReference;

/**
 * The purpose of UserMock is to act exactly the same as a User,
 * EXCEPT the profilePicture *requirement*. This allows us to test things
 * that use User, since Bitmaps cannot be created in pure Java as they
 * are Android specific objects.
 * Use null as profile picture for testing with this mock class
 */
public class UserMock extends User {
    public UserMock(String uniqueID, String name, String email, Bitmap profilePicture) throws Exception {
        super(uniqueID, name, email);
    }

    public UserMock(String uniqueID, String name, String email, Long phoneNumber, Bitmap profilePicture) throws Exception {
        super(uniqueID, name, email, phoneNumber, profilePicture);
    }

    public UserMock(String uniqueID, String name, String email, Long phoneNumber, Bitmap profilePicture, boolean isAdmin, boolean receivesOrgAdmNotifications, DocumentReference userRef, Facility facility) throws Exception {
        super(uniqueID, name, email, phoneNumber, profilePicture, isAdmin, receivesOrgAdmNotifications, userRef, facility);
    }

    @Override
    public void setProfilePicture(Bitmap profilePicture) {

    }
}
