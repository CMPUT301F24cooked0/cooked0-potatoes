package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

public class EntrantStatusUnitTest {
    @Test
    public void firstConstructorTest() throws Exception {
        // test that constructor doesn't cause errors
        LatLng location = new LatLng(69.420, 42.69);
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        EntrantStatus entrantStatus = new EntrantStatus(user, location);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        Status status = Status.none;
        EntrantStatus entrantStatus = new EntrantStatus(user, location, status);
    }

    @Test
    public void gettersAndSettersTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
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
}
