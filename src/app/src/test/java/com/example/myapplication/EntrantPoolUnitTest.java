package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

public class EntrantPoolUnitTest {
    FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);

    @Test
    public void constructorTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
    }

    @Test
    public void addEntrantNotInPoolTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void addEntrantInPoolTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertThrows(EntrantAlreadyInPool.class, () -> {entrantPool.addEntrant(entrant, location);});
    }

    @Test
    public void removeEntrantNotInPoolTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        entrantPool.removeEntrant(entrant);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void removeEntrantInPoolTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
        entrantPool.removeEntrant(entrant);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void setEntrantStatusTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.notChosen);
    }

    @Test
    public void setEntrantStatusNotInPoolTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        entrantPool.setEntrantStatus(entrant, Status.notChosen);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsNoEntrantsTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        assertEquals(entrantPool.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrants().size(), 1);
        assertEquals(entrantPool.getEntrants().get(0), entrant);
    }

    @Test
    public void getEntrantStatusesNoEntrantsTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        assertEquals(entrantPool.getEntrantStatuses().size(), 0);
    }

    @Test
    public void getEntrantStatusesTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        entrantPool.addEntrant(entrant, location);
        assertEquals(entrantPool.getEntrantStatuses().size(), 1);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getEntrant(), entrant);
        assertEquals(entrantPool.getEntrantStatuses().get(0).getStatus(), Status.none);
    }

    @Test
    public void drawEntrantsNoEntrantsTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng facilityLocation = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", facilityLocation, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
        EntrantPool entrantPool = new EntrantPool(event, db);
        entrantPool.drawEntrants(0);
        // TODO finish writing tests for drawEntrants when functionality is added
    }
}
