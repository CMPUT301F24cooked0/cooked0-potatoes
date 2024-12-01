package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Use EventMock instead of Event for testing, as it removes the
 * eventPoster requirement. See note on EventMock class for more info
 */
public class EventUnitTest {
    @Test
    public void firstConstructorTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, 123, false);
    }

    @Test
    public void invalidateQRCodeTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
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
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        String name = "newName";
        assertNotEquals(event.getName(), name);
        event.setName(name);
        assertEquals(event.getName(), name);
    }

    @Test
    public void setGetDateTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2027-01-01T01:00:00.00Z"), Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2027-01-01T01:00:00.00Z"), null, false);
        TimeUnit.MILLISECONDS.sleep(10); // to ensure the new Date object isn't actually the same
        Instant instant = Instant.parse("2026-01-01T00:00:00.00Z");
        assertNotEquals(event.getStartInstant(), instant);
        event.setStartInstant(instant);
        assertEquals(event.getStartInstant(), instant);
    }

    @Test
    public void setGetCapacityTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
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
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        event.setEventPoster(null);
        assertNull(event.getEventPoster());
    }

    @Test
    public void setGetQrCodeTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        QRCode qrCode = new QRCode("text");
        event.setQrCode(qrCode);
        assertNotNull(event.getQrCode());
        assertNotNull(event.getQrCode().getText());
        assertEquals(event.getQrCode(), qrCode);
    }

    @Test
    public void addEntrantTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrants().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrants().size(), 1);
        assertEquals(event.getEntrants().get(0), entrant);
    }

    @Test
    public void addDuplicateEntrantTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        event.addEntrant(entrant, joinedFrom);
        assertThrows(EntrantAlreadyInPool.class, () -> {event.addEntrant(entrant, joinedFrom);});
        assertEquals(event.getEntrants().size(), 1);
    }

    @Test
    public void removeEntrantTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
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
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        assertEquals(event.getEntrants().size(), 0);
        event.removeEntrant(entrant);
        assertEquals(event.getEntrants().size(), 0);
    }

    @Test
    public void getEntrantsTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrants().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrants().size(), 1);
        assertEquals(event.getEntrants().get(0), entrant);
    }

    @Test
    public void getEntrantStatusesTest() throws Exception {
        Event event = new EventMock("name", Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2023-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"), null, false);
        User entrant = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        LatLng joinedFrom = new LatLng(42.69, 69.42);
        assertEquals(event.getEntrantStatuses().size(), 0);
        event.addEntrant(entrant, joinedFrom);
        assertEquals(event.getEntrantStatuses().size(), 1);
        assertEquals(event.getEntrantStatuses().get(0).getEntrant(), entrant);
        assertEquals(event.getEntrantStatuses().get(0).getStatus(), Status.none);
    }
}
