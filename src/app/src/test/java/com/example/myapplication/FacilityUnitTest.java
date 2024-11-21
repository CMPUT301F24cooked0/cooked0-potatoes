package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.time.Instant;

public class FacilityUnitTest {
    @Test
    public void constructorTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
    }

    @Test
    public void addEventTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        Event event = new EventMock("name", Instant.now(), null);
        assertEquals(facility.getEvents().size(), 0);
        facility.addEvent(event);
        assertEquals(facility.getEvents().size(), 1);
        assertEquals(facility.getEvents().get(0), event);
    }

    @Test
    public void addDuplicateEventTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        Event event = new EventMock("name", Instant.now(), null);
        facility.addEvent(event);
        assertThrows(EventAlreadyExistsAtFacility.class, () -> {facility.addEvent(event);});
    }

    @Test
    public void deleteEventNotInFacilityTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        Event event = new EventMock("name", Instant.now(), null);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteEventInFacilityTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        Event event = new EventMock("name", Instant.now(), null);
        facility.addEvent(event);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteAllEventsTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        Event event = new EventMock("name", Instant.now(), null);
        facility.addEvent(event);
        facility.deleteAllEvents();
        assertEquals(facility.getEvents().size(), 0);
    }
}
