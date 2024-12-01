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
        String address = "address";
        Facility facility = new Facility("name", location, address);
    }

    @Test
    public void addEventTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), null, false);
        assertEquals(facility.getEvents().size(), 0);
        facility.addEvent(event);
        assertEquals(facility.getEvents().size(), 1);
        assertEquals(facility.getEvents().get(0), event);
    }

    @Test
    public void addDuplicateEventTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), null, false);
        facility.addEvent(event);
        assertThrows(EventAlreadyExistsAtFacility.class, () -> {facility.addEvent(event);});
    }

    @Test
    public void deleteEventNotInFacilityTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), null, false);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteEventInFacilityTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), null, false);
        facility.addEvent(event);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteAllEventsTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), null, false);
        facility.addEvent(event);
        facility.deleteAllEvents();
        assertEquals(facility.getEvents().size(), 0);
    }
}
