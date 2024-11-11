package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

public class EntrantStatusUnitTest {
    @Test
    public void firstConstructorTest() throws Exception {
        // test that constructor doesn't cause errors
        LatLng location = new LatLng(69.420, 42.69);
        User user = new User("name", "email@email.com");
        Facility facility = new Facility("name", new LatLng(42.69, 69.42), user);
        Event event = new EventMock("name", new Date(), null, facility);
        EntrantStatus entrantStatus = new EntrantStatus(user, location, event);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        User user = new User("name", "email@email.com");
        Facility facility = new Facility("name", new LatLng(42.69, 69.42), user);
        Event event = new EventMock("name", new Date(), null, facility);
        LatLng location = new LatLng(69.420, 42.69);
        Status status = Status.none;
        EntrantStatus entrantStatus = new EntrantStatus(user, location, status, event);
    }

    @Test
    public void gettersAndSettersTest() throws Exception {
        User user = new User("name", "email@email.com");
        Facility facility = new Facility("name", new LatLng(42.69, 69.42), user);
        Event event = new EventMock("name", new Date(), null, facility);
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(user, location, event);

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

    @Test
    public void sendNotificationTest() throws Exception {
        User user = new User("name", "email@email.com");
        Facility facility = new Facility("name", new LatLng(42.69, 69.42), user);
        Event event = new EventMock("name", new Date(), null, facility);
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(user, location, event);

        entrantStatus.sendNotification("notification");
        // TODO test that NotificationSender.sendNotification was called
        // I tried to do this using Mockito but it's really weird and complicated for some reason
    }
}
