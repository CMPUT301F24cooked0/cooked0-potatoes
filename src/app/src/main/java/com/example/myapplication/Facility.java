package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Facility {
    private final String name;
    private final LatLng location;
    private User user;
    private ArrayList<Event> events;
    private FirebaseFirestore db;
    private DocumentReference facilityRef;
    private CollectionReference eventsCol;
    private DocumentReference userRef;

    public Facility(String name, LatLng location, User user) {
        this.name = name;
        this.location = location;
        this.user = user;
        this.events = new ArrayList<Event>();
        // update database after creating facility
        db = FirebaseFirestore.getInstance();
        userRef = user.getUserRef();
        facilityRef = userRef.collection("facility").document();
        eventsCol = facilityRef.collection("events");
        HashMap<String, Object> facilityData = new HashMap<>();
        facilityData.put("name", name);
        facilityData.put("location", location);
        facilityRef.set(facilityData);

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


    }

    public void deleteEvent(Event event) {
        if (event == null) {
            return; // nothing to delete
        }
        if (!this.events.contains(event)) {
            return; // event does not exist at this facility, nothing to delete
        }
        this.events.remove(event);
        eventsCol.document(event.getEventId()).delete(); // delete event from database
    }

    public void deleteAllEvents() {
        eventsCol.get().addOnCompleteListener(task -> { // get all events in facility
            if (task.isSuccessful()) {
                for (Event event : this.events) {
                    eventsCol.document(event.getEventId()).delete(); // delete event from database
                }
            }
        });
        this.events.clear(); // clear events list
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }

    public DocumentReference getFacilityRef() {
        return this.facilityRef; // return reference to facility in database
    }
}

