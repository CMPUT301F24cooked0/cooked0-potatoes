package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

public class FacilityUnitTest {
    FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);

    @Test
    public void constructorTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
    }

    @Test
    public void addEventTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        assertEquals(facility.getEvents().size(), 0);
        facility.addEvent(event);
        assertEquals(facility.getEvents().size(), 1);
        assertEquals(facility.getEvents().get(0), event);
    }

    @Test
    public void addDuplicateEventTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        facility.addEvent(event);
        assertThrows(EventAlreadyExistsAtFacility.class, () -> {facility.addEvent(event);});
    }

    @Test
    public void deleteEventNotInFacilityTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteEventInFacilityTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        facility.addEvent(event);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteAllEventsTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        facility.addEvent(event);
        facility.deleteAllEvents();
        assertEquals(facility.getEvents().size(), 0);
    }
}
