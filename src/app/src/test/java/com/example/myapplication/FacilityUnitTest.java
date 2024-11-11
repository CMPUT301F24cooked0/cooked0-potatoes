package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

public class FacilityUnitTest {
    @Test
    public void constructorTest() throws Exception {
        User user = new User(null, "name", "email@email.com");
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user);
    }

    @Test
    public void addEventTest() throws Exception {
        User user = new User(null, "name", "email@email.com");
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user);
        Event event = new EventMock("name", new Date(), null, facility);
        assertEquals(facility.getEvents().size(), 0);
        facility.addEvent(event);
        assertEquals(facility.getEvents().size(), 1);
        assertEquals(facility.getEvents().get(0), event);
    }

    @Test
    public void addDuplicateEventTest() throws Exception {
        User user = new User(null, "name", "email@email.com");
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user);
        Event event = new EventMock("name", new Date(), null, facility);
        facility.addEvent(event);
        assertThrows(EventAlreadyExistsAtFacility.class, () -> {facility.addEvent(event);});
    }

    @Test
    public void deleteEventNotInFacilityTest() throws Exception {
        User user = new User(null, "name", "email@email.com");
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user);
        Event event = new EventMock("name", new Date(), null, facility);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteEventInFacilityTest() throws Exception {
        User user = new User(null, "name", "email@email.com");
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user);
        Event event = new EventMock("name", new Date(), null, facility);
        facility.addEvent(event);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteAllEventsTest() throws Exception {
        User user = new User(null, "name", "email@email.com");
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user);
        Event event = new EventMock("name", new Date(), null, facility);
        facility.addEvent(event);
        facility.deleteAllEvents();
        assertEquals(facility.getEvents().size(), 0);
    }
}
