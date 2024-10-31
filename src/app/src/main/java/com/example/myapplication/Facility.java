package com.example.myapplication;

import android.location.Location;

import java.util.ArrayList;

public class Facility {
    private final String name;
    private final Location location;
    private ArrayList<Event> events;

    public Facility(String name, Location location) {
        this.name = name;
        this.location = location;
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
    }

    public void deleteAllEvents() {
        this.events.clear();
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }
}
