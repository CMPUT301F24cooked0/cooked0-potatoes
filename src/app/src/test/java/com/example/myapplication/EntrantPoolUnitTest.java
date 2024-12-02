package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class EntrantPoolUnitTest {
    @Test
    public void constructorTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
    }

    @Test
    public void addEntrantNotInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void addEntrantInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertThrows(EntrantAlreadyInPool.class, () -> {entrantPool.addEntrant(entrant, location);});
    }

    @Test
    public void removeEntrantNotInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        entrantPool.removeEntrant(entrant);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void removeEntrantInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
        entrantPool.removeEntrant(entrant);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void setEntrantStatusTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.notChosen);
    }

    @Test
    public void setEntrantStatusNotInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsNoEntrantsTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void getEntrantStatusesNoEntrantsTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        assertEquals(entrantPool.getEntrantStatuses().size(), 0);
    }

    @Test
    public void getEntrantStatusesTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrantStatuses().size(), 1);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getEntrant(), entrant);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.none);
    }

    @Test
    public void drawEntrantsNoEntrantsTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        ArrayList<User> drawnEntrants = entrantPool.drawEntrants(0);
        assertEquals(drawnEntrants.size(), 0);
        drawnEntrants = entrantPool.drawEntrants(100);
        assertEquals(drawnEntrants.size(), 0);
    }

    @Test
    public void drawEntrantsNotEnoughTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        ArrayList<User> drawnEntrants = entrantPool.drawEntrants(100);
        assertEquals(drawnEntrants.size(), 1);
        assertEquals(drawnEntrants.get(0), entrant);
    }

    @Test
    public void drawEntrantsTooManyTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng location = new LatLng(69.420, 42.69);
        User entrant2 = new UserMock(null, "name2", "email2@email.ca", (Bitmap) null);
        LatLng location2 = new LatLng(69.421, 42.69);
        User entrant3 = new UserMock(null, "name3", "email3@email.ca", (Bitmap) null);
        LatLng location3 = new LatLng(69.422, 42.69);
        entrantPool.addEntrant(entrant, location);
        entrantPool.addEntrant(entrant2, location2);
        entrantPool.addEntrant(entrant3, location3);
        ArrayList<User> drawnEntrants = entrantPool.drawEntrants(2);
        assertEquals(drawnEntrants.size(), 2);
    }
}
