package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

// TODO move the EventMock class creation to its own file

public class FacilityUnitTest {
    @Test
    public void constructorTest() {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
    }

    @Test
    public void addEventTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        // create this EventMock class to remove the need to have a non-null event poster
        class EventMock extends Event {

            public EventMock(String name, Date date, Bitmap eventPoster) throws Exception {
                super(name, date, eventPoster);
            }

            @Override
            public void setEventPoster(Bitmap eventPoster) throws Exception {

            }
        }
        Event event = new EventMock("name", new Date(), null);
        assertEquals(facility.getEvents().size(), 0);
        facility.addEvent(event);
        assertEquals(facility.getEvents().size(), 1);
        assertEquals(facility.getEvents().get(0), event);
    }

    @Test
    public void addDuplicateEventTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        // create this EventMock class to remove the need to have a non-null event poster
        class EventMock extends Event {

            public EventMock(String name, Date date, Bitmap eventPoster) throws Exception {
                super(name, date, eventPoster);
            }

            @Override
            public void setEventPoster(Bitmap eventPoster) throws Exception {

            }
        }
        Event event = new EventMock("name", new Date(), null);
        facility.addEvent(event);
        assertThrows(EventAlreadyExistsAtFacility.class, () -> {facility.addEvent(event);});
    }

    @Test
    public void deleteEventNotInFacilityTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        // create this EventMock class to remove the need to have a non-null event poster
        class EventMock extends Event {

            public EventMock(String name, Date date, Bitmap eventPoster) throws Exception {
                super(name, date, eventPoster);
            }

            @Override
            public void setEventPoster(Bitmap eventPoster) throws Exception {

            }
        }
        Event event = new EventMock("name", new Date(), null);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteEventInFacilityTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        // create this EventMock class to remove the need to have a non-null event poster
        class EventMock extends Event {

            public EventMock(String name, Date date, Bitmap eventPoster) throws Exception {
                super(name, date, eventPoster);
            }

            @Override
            public void setEventPoster(Bitmap eventPoster) throws Exception {

            }
        }
        Event event = new EventMock("name", new Date(), null);
        facility.addEvent(event);
        facility.deleteEvent(event);
        assertEquals(facility.getEvents().size(), 0);
    }

    @Test
    public void deleteAllEventsTest() throws Exception {
        LatLng location = new LatLng(69.420, 42.69);
        Facility facility = new Facility("name", location);
        // create this EventMock class to remove the need to have a non-null event poster
        class EventMock extends Event {

            public EventMock(String name, Date date, Bitmap eventPoster) throws Exception {
                super(name, date, eventPoster);
            }

            @Override
            public void setEventPoster(Bitmap eventPoster) throws Exception {

            }
        }
        Event event = new EventMock("name", new Date(), null);
        facility.addEvent(event);
        facility.deleteAllEvents();
        assertEquals(facility.getEvents().size(), 0);
    }
}
