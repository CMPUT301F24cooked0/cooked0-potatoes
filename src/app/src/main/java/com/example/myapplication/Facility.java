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
    // String address representation of the location coordinates, for displaying to the user
    private String address;
    private LatLng location;
    private ArrayList<Event> events;
    private DocumentReference facilityRef;

    /**
     * Simplest public constructor
     * @param name
     * @param location
     */
    public Facility(String name, LatLng location, String address) throws Exception {
        this.setName(name);
        this.setLocation(location);
        this.setAddress(address);
        this.events = new ArrayList<Event>();
    }

    /**
     * only use this constructor in DatabaseManager to instantiate a facility from the data in the database
     * @param name
     * @param location
     * @param facilityRef
     * @param events
     */
    public Facility(String name, LatLng location, String address, DocumentReference facilityRef, ArrayList<Event> events) throws Exception {
        this(name, location, address);
        this.setFacilityReference(facilityRef);
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
    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("Facility name cannot be null");
        }
        this.name = name;
    }

    /**
     * edit this facility's location
     * @param location
     */
    public void setLocation(LatLng location) throws Exception {
        if (location == null) {
            throw new Exception("Facility location cannot be null");
        }
        this.location = location;
    }

    /**
     * edit this facility's address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * get the list of events at this facility
     * @return
     */
    public ArrayList<Event> getEvents() {
        return this.events;
    }

    /**
     * Set this facility's DocumentReference (in the database)
     * @param facilityRef
     */
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

    /**
     * get this facility's address
     * @return
     */
    public String getAddress() {
        return this.address;
    }
}
