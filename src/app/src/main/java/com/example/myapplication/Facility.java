package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class Facility {
    private final String name;
    private final LatLng location;
    private ArrayList<Event> events;
    private DocumentReference facilityRef;

    private Facility(String name, LatLng location) {
        this.name = name;
        this.location = location;
        this.events = new ArrayList<Event>();
    }

    public Facility(String name, LatLng location, User user) {
        this(name, location);
        this.facilityRef = new DatabaseManager().createFacility(user, this);
    }

    /**
     * only use this constructor in DatabaseManager to instantiate a facility from the data in the database
     * @param name
     * @param location
     * @param facilityRef
     * @param events
     */
    public Facility(String name, LatLng location, DocumentReference facilityRef, ArrayList<Event> events) {
        this(name, location);
        this.facilityRef = facilityRef;
        this.events = events;
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
        eventsCol.document(event.getEventId()); // add event to database
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

    public String getName() {
        return this.name;
    }

    public LatLng getLocation() {
        return this.location;
    }
}
