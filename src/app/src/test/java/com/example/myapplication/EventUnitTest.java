package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Use EventMock instead of Event for testing, as it removes the
 * eventPoster requirement. See note on EventMock class for more info
 */
public class EventUnitTest {
    FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);

    @Test
    public void firstConstructorTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(),  null, facility, db);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, 123, db);
    }

    @Test
    public void invalidateQRCodeTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        QRCode qrCode = new QRCode("text");
        event.setQrCode(qrCode);
        assertNotNull(event.getQrCode());
        assertNotNull(event.getQrCode().getText());
        event.invalidateQRCode();
        assertNotNull(event.getQrCode());
        assertNull(event.getQrCode().getText());
    }

    @Test
    public void setGetNameTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        String name = "newName";
        assertNotEquals(event.getName(), name);
        event.setName(name);
        assertEquals(event.getName(), name);
    }

    @Test
    public void setGetDateTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        TimeUnit.MILLISECONDS.sleep(10); // to ensure the new Date object isn't actually the same
        Date date = new Date();
        assertNotEquals(event.getDate(), date);
        event.setDate(date);
        assertEquals(event.getDate(), date);
    }

    @Test
    public void setGetCapacityTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        Integer capacity = 123;
        assertNull(event.getCapacity());
        event.setCapacity(capacity);
        assertNotNull(event.getCapacity());
        assertEquals(event.getCapacity(), capacity);
    }

    @Test
    public void getSetEventPosterTest() throws Exception {
        // can't really test this, EventMock overwrites setter, and only null can be used in testing
        // in reality Event should not allow setting a null eventPoster, but we can't test it...
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        event.setEventPoster(null);
        assertNull(event.getEventPoster());
    }

    @Test
    public void setGetQrCodeTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        QRCode qrCode = new QRCode("text");
        event.setQrCode(qrCode);
        assertNotNull(event.getQrCode());
        assertNotNull(event.getQrCode().getText());
        assertEquals(event.getQrCode(), qrCode);
    }

    @Test
    public void addEntrantTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrants().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrants().size(), 1);
        assertEquals(event.getEntrants().get(0), entrant);
    }

    @Test
    public void addDuplicateEntrantTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        event.addEntrant(entrant, joinedFrom);
        assertThrows(EntrantAlreadyInPool.class, () -> {event.addEntrant(entrant, joinedFrom);});
        assertEquals(event.getEntrants().size(), 1);
    }

    @Test
    public void removeEntrantTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrants().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrants().size(), 1);
        assertEquals(event.getEntrants().get(0), entrant);
        event.removeEntrant(entrant);
        assertEquals(event.getEntrants().size(), 0);
    }

    @Test
    public void removeEntrantNotInTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        User entrant = new User("name", "email@email.ca", db);
        assertEquals(event.getEntrants().size(), 0);
        event.removeEntrant(entrant);
        assertEquals(event.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrants().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrants().size(), 1);
        assertEquals(event.getEntrants().get(0), entrant);
    }

    @Test
    public void getEntrantStatusesTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location, user, db);
        Event event = new EventMock("name", new Date(), null, facility, db);
        User entrant = new User("name", "email@email.ca", db);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrantStatuses().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrantStatuses().size(), 1);
        assertEquals(event.getEntrantStatuses().get(0).getEntrant(), entrant);
        assertEquals(event.getEntrantStatuses().get(0).getStatus(), Status.none);
    }
}
