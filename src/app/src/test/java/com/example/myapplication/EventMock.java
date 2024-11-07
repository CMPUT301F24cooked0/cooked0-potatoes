package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * The purpose of EventMock is to act exactly the same as an Event,
 * EXCEPT the eventPoster *requirement*. This allows us to test things
 * that use Event, since Bitmaps cannot be created in pure Java as they
 * are Android specific objects.
 * Use null as event poster for testing with this mock class
 */
class EventMock extends Event {

    public EventMock(String name, Date date, Bitmap eventPoster, Facility facility, FirebaseFirestore db) throws Exception {
        super(name, date, eventPoster, facility, db);
    }

    public EventMock(String name, Date date, Bitmap eventPoster, Facility facility, Integer capacity, FirebaseFirestore db) throws Exception {
        super(name, date, eventPoster, facility, capacity, db);
    }

    @Override
    public void setEventPoster(Bitmap eventPoster) throws Exception {

    }
}
