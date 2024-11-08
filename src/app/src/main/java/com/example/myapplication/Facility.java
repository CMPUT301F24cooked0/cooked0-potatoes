package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

/*
This class is responsible for making a facility object. It sets facility details as well as
retrieves them. The class is also responsible for adding and removing events created by organizers.
 */
public class Facility {
    private String name;
    private LatLng location;
    private ArrayList<Event> events;
    private DocumentReference facilityRef;

    /**
     * Base constructor to consolidate code used by other constructors
     * @param name
     * @param location
     */
    private Facility(String name, LatLng location) {
        this.name = name;
        this.location = location;
        this.events = new ArrayList<Event>();
    }

    /**
     * Simplest public constructor, which **creates a new facility** ond adds it to the database
     * @param name
     * @param location
     * @param user
     */
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

    /**
     * adds an event to this facility. throws an exception if the event already exists at this facility
     * @param event
     */
    public void addEvent(Event event) throws EventAlreadyExistsAtFacility {
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

    /**
     * deletes an event from this facility. does nothing if the event is not in this facility
     * @param event
     */
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

    /**
     * deletes all events from this facility
     */
    public void deleteAllEvents() {
        this.events.clear(); // clear events list
        new DatabaseManager().updateFacility(this);
    }

    /**
     * get the list of events at this facility
     * @return
     */
    public ArrayList<Event> getEvents() {
        return this.events;
    }

    /**
     * get the DocumentReference for this facility in the database
     * @return
     */
    public DocumentReference getFacilityReference() {
        return this.facilityRef; // return reference to facility in database
    }

    /**
     * get this facility's name
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * get this facility's location
     * @return
     */
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
