package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class Facility {
    private String name;
    private LatLng location;
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
        for (Event event : events) {
            this.addEvent(event);
        }
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
        new DatabaseManager().updateFacility(this);
    }

    public void deleteEvent(Event event) {
        if (event == null) {
            return; // nothing to delete
        }
        if (!this.events.contains(event)) {
            return; // event does not exist at this facility, nothing to delete
        }
        this.events.remove(event);
        new DatabaseManager().updateFacility(this);
    }

    public void deleteAllEvents() {
        this.events.clear(); // clear events list
        new DatabaseManager().updateFacility(this);
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }

    public DocumentReference getFacilityReference() {
        return this.facilityRef; // return reference to facility in database
    }

    public String getName() {
        return this.name;
    }

    public LatLng getLocation() {
        return this.location;
    }
    public void setName(String name) {
        this.name = name;
        // TODO update database
    }
    public void setLocation(LatLng location) {
        this.location = location;
        // TODO update database
    }

}
