package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class EntrantPoolUnitTest {
    @Test
    public void constructorTest() {
        EntrantPool entrantPool = new EntrantPool();
    }

    @Test
    public void addEntrantNotInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new User("name", "email");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void addEntrantInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new User("name", "email");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        User entrant2 = new User("name2", "email2");
        assertThrows(EntrantAlreadyInPool.class, () -> {entrantPool.addEntrant(entrant2, location);});
    }

    @Test
    public void removeEntrantNotInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new User("name", "email");
        entrantPool.removeEntrant(entrant);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void removeEntrantInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new User("name", "email");
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
        User entrant = new User("name", "email");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.notChosen);
    }

    @Test
    public void setEntrantStatusNotInPoolTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new User("name", "email");
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsNoEntrantsTest() {
        EntrantPool entrantPool = new EntrantPool();
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new User("name", "email");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void getEntrantStatusesNoEntrantsTest() {
        EntrantPool entrantPool = new EntrantPool();
        assertEquals(entrantPool.getEntrantStatuses().size(), 0);
    }

    @Test
    public void getEntrantStatusesTest() throws Exception {
        EntrantPool entrantPool = new EntrantPool();
        User entrant = new User("name", "email");
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrantStatuses().size(), 1);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getEntrant(), entrant);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.none);
    }

    @Test
    public void drawEntrantsNoEntrantsTest() {
        EntrantPool entrantPool = new EntrantPool();
        entrantPool.drawEntrants(0);
        // TODO finish writing tests for drawEntrants when functionality is added
    }
}
