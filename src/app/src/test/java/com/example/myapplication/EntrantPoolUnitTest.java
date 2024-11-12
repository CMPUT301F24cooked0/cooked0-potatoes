package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

public class EntrantPoolUnitTest {
    @Test
    public void constructorTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
    }

    @Test
    public void addEntrantNotInPoolTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void addEntrantInPoolTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertThrows(EntrantAlreadyInPool.class, () -> {entrantPool.addEntrant(entrant, location);});
    }

    @Test
    public void removeEntrantNotInPoolTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        entrantPool.removeEntrant(entrant);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void removeEntrantInPoolTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
        entrantPool.removeEntrant(entrant);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void setEntrantStatusTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.notChosen);
    }

    @Test
    public void setEntrantStatusNotInPoolTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsNoEntrantsTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void getEntrantStatusesNoEntrantsTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        assertEquals(entrantPool.getEntrantStatuses().size(), 0);
    }

    @Test
    public void getEntrantStatusesTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrantStatuses().size(), 1);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getEntrant(), entrant);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.none);
    }

    @Test
    public void drawEntrantsNoEntrantsTest() throws Exception {
        Event event = new EventMock("name", new Date(), null);
        EntrantPool entrantPool = new EntrantPool(event);
        entrantPool.drawEntrants(0);
        // TODO finish writing tests for drawEntrants when functionality is added
    }
}
