package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Facility {
    private final String name;
    private final LatLng location;
    private ArrayList<Event> events;
    private FirebaseFirestore db;
    private CollectionReference facilitiesRef;
    private CollectionReference eventsRef;

    public Facility(String name, LatLng location) {
        this.name = name;
        this.location = location;
        this.events = new ArrayList<Event>();
        // update database after creating facility
        db = FirebaseFirestore.getInstance();
        facilitiesRef = this.db.collection("facilities");
        eventsRef = facilitiesRef.document(this.name).collection("events");
        HashMap<String, Object> facilityData = new HashMap<>();
        facilityData.put("location", location);
        facilitiesRef.document(this.name).set(facilityData);

    }

    public void addEvent(Event event) {
        if (event == null) {
            return;
        }
        if (this.events.contains(event)) {
            // entrant is already in this pool, they cannot be added again
            throw new EventAlreadyExistsAtFacility("this event already exists at this facility and cannot be added again");
        }
        this.events.add(event);

        // update database after adding event
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("date", event.getDate());
        eventData.put("capacity", event.getCapacity());
        // TODO store event poster in database through reference (can't store image directly)
        // TODO storing entrant pool in database (collection or subcollection of event?)
        eventData.put("qrCode", event.getQrCode().getText());
        eventsRef.document(event.getName()).set(eventData);

    }

    public void deleteEvent(Event event) {
        if (event == null) {
            return; // nothing to delete
        }
        if (!this.events.contains(event)) {
            return; // event does not exist at this facility, nothing to delete
        }
        this.events.remove(event);
        // update database after removing event
        eventsRef.document(event.getName()).delete();
    }

    public void deleteAllEvents() {
        this.events.clear();
        // update database after removing all events from facility
        eventsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (Event event : this.events) {
                    eventsRef.document(event.getName()).delete();
                }
            }
        });
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }
}

