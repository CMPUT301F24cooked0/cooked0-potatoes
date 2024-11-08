package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/*
This class is responsible for making a facility object. It sets facility details as well as
retrieves them. The class is also responsible for adding and removing events created by organizers.
 */
public class Facility {
    private String name;
    private LatLng location;
    private ArrayList<Event> events;


    /**
     * Base constructor to consolidate code used by other constructors
     * @param name
     * @param location
     */
    public Facility(String name, LatLng location) {
        this.name = name;
        this.location = location;
        this.events = new ArrayList<Event>();
        // TODO update database
    }

    /**
     * adds an event to this facility. throws an exception if the event already exists at this facility
     * @param event
     */
    public void addEvent(Event event) {
        if (event == null) {
            return;
        }
        if (this.events.contains(event)) {
            // entrant is already in this pool, they cannot be added again
            throw new EventAlreadyExistsAtFacility("this event already exists at this facility and cannot be added again");
        }
        this.events.add(event);
        // TODO update database
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
        // TODO update database
    }


    /**
     * deletes all events from this facility
     */
    public void deleteAllEvents() {
        this.events.clear();
        // TODO update database
    }

    /**
     * get the list of events at this facility
     * @return
     */
    public ArrayList<Event> getEvents() {
        return this.events;
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
