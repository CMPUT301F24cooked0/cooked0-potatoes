package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class EntrantStatusUnitTest {
    @Test
    public void firstConstructorTest() {
        // test that constructor doesn't cause errors
        User user = null;
        try {
            user = new User("name", "email@test.ca");
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(user, location);
    }

    @Test
    public void secondConstructorTest() {
        User user = null;
        try {
            user = new User("name", "email@test.ca");
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        Status status = Status.none;
        EntrantStatus entrantStatus = new EntrantStatus(user, location, status);
    }

    @Test
    public void gettersAndSettersTest() {
        User user = null;
        try {
            user = new User("name", "email@test.ca");
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(user, location);

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
    public void sendNotificationTest() {
        User user = null;
        try {
            user = new User("name", "email@test.ca");
        }
        catch (Exception exception) {
            fail();
        }
        LatLng location = new LatLng(69.420, 42.69);
        EntrantStatus entrantStatus = new EntrantStatus(user, location);

        entrantStatus.sendNotification("notification");
        // TODO test that NotificationSender.sendNotification was called
        // I tried to do this using Mockito but it's really weird and complicated for some reason
    }
}
