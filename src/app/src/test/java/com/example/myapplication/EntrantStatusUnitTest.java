package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

public class EntrantStatusUnitTest {
    FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);

    @Test
    public void firstConstructorTest() throws Exception {
        // test that constructor doesn't cause errors
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = null;
        try {
            entrant = new User("name", "email@test.ca", db);
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(entrant, location, entrantPool, db);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = null;
        try {
            entrant = new User("name", "email@test.ca", db);
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        Status status = Status.none;
        EntrantStatus entrantStatus = new EntrantStatus(entrant, location, entrantPool, status, db);
    }

    @Test
    public void gettersAndSettersTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = null;
        try {
            entrant = new User("name", "email@test.ca", db);
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(entrant, location, entrantPool, db);

        // test getters
        User outputUser = entrantStatus.getEntrant();
        assertEquals(user, outputUser);
        Status outputStatus = entrantStatus.getStatus();
        assertEquals(Status.none, outputStatus);
        LatLng outputLocation = entrantStatus.getJoinedFrom();
        assertEquals(location, outputLocation);

        // test setters
        entrantStatus.setStatus(Status.notChosen);
        outputStatus = entrantStatus.getStatus();
        assertEquals(Status.notChosen, outputStatus);
    }

    NotificationSender notificationSenderMock;

    @Test
    public void sendNotificationTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = null;
        try {
            entrant = new User("name", "email@test.ca", db);
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(entrant, location, entrantPool, db);

        entrantStatus.sendNotification("notification");
        // TODO test that NotificationSender.sendNotification was called
        // I tried to do this using Mockito but it's really weird and complicated for some reason
    }
}
