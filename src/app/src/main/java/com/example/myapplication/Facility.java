package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Facility {
    private final String name;
    private final LatLng location;
    private ArrayList<Event> events;

    public Facility(String name, LatLng location) {
        this.name = name;
        this.location = location;
        this.events = new ArrayList<Event>();
        // TODO update database
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
        // TODO update database
    }

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

    public void deleteAllEvents() {
        this.events.clear();
        // TODO update database
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }
}
