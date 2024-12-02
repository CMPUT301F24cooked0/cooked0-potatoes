package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;


public class MockEntrantPool extends EntrantPool {

    public MockEntrantPool() {
        super();
        try {
            // Add mock users to the entrant pool
            addEntrant(new User("1", "syntax", "syntax@gmail.com"), new LatLng(0, 0));
            addEntrant(new User("User 2","User 2" ,"user2@example.com"), new LatLng(1, 1));
            addEntrant(new User("User 3", "User 3", "user3@example.com"), null); // No location for this user
        } catch (EntrantAlreadyInPool e) {
            e.printStackTrace(); // This should not happen in this mock setup
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
