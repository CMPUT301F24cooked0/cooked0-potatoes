package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Use EventMock instead of Event for testing, as it removes the
 * eventPoster requirement. See note on EventMock class for more info
 */
public class EventUnitTest {
    @Test
    public void firstConstructorTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null, 123);
    }

    @Test
    public void invalidateQRCodeTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
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
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        String name = "newName";
        assertNotEquals(event.getName(), name);
        event.setName(name);
        assertEquals(event.getName(), name);
    }

    @Test
    public void setGetDateTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        TimeUnit.MILLISECONDS.sleep(10); // to ensure the new Date object isn't actually the same
        Date date = new Date(2026, 1, 1);
        assertNotEquals(event.getDate(), date);
        event.setDate(date);
        assertEquals(event.getDate(), date);
    }

    @Test
    public void setGetCapacityTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
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
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        event.setEventPoster(null);
        assertNull(event.getEventPoster());
    }

    @Test
    public void setGetQrCodeTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        QRCode qrCode = new QRCode("text");
        event.setQrCode(qrCode);
        assertNotNull(event.getQrCode());
        assertNotNull(event.getQrCode().getText());
        assertEquals(event.getQrCode(), qrCode);
    }

    @Test
    public void addEntrantTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrants().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrants().size(), 1);
        assertEquals(event.getEntrants().get(0), entrant);
    }

    @Test
    public void addDuplicateEntrantTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        event.addEntrant(entrant, joinedFrom);
        assertThrows(EntrantAlreadyInPool.class, () -> {event.addEntrant(entrant, joinedFrom);});
        assertEquals(event.getEntrants().size(), 1);
    }

    @Test
    public void removeEntrantTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        User entrant = new User(null, "name", "email@email.ca");
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
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        User entrant = new User(null, "name", "email@email.ca");
        assertEquals(event.getEntrants().size(), 0);
        event.removeEntrant(entrant);
        assertEquals(event.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrants().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrants().size(), 1);
        assertEquals(event.getEntrants().get(0), entrant);
    }

    @Test
    public void getEntrantStatusesTest() throws Exception {
        Event event = new EventMock("name", new Date(2025, 1, 1), null);
        User entrant = new User(null, "name", "email@email.ca");
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrantStatuses().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrantStatuses().size(), 1);
        assertEquals(event.getEntrantStatuses().get(0).getEntrant(), entrant);
        assertEquals(event.getEntrantStatuses().get(0).getStatus(), Status.none);
    }
}
