package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

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
    }

    /**
     * deletes all events from this facility
     */
    public void deleteAllEvents() {
        this.events.clear(); // clear events list
    }

    /**
     * edit this facility's name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * edit this facility's location
     * @param location
     */
    public void setLocation(LatLng location) {
        this.location = location;
    }

    /**
     * get the list of events at this facility
     * @return
     */
    public ArrayList<Event> getEvents() {
        return this.events;
    }

    public void setFacilityReference(DocumentReference facilityRef) {
        this.facilityRef = facilityRef;
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
}
