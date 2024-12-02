package com.example.myapplication;

import android.graphics.Bitmap;

import java.time.Instant;
import java.util.Date;

/**
 * The purpose of EventMock is to act exactly the same as an Event,
 * EXCEPT the eventPoster *requirement*. This allows us to test things
 * that use Event, since Bitmaps cannot be created in pure Java as they
 * are Android specific objects.
 * Use null as event poster for testing with this mock class
 */
class EventMock extends Event {

    public EventMock(String name, Instant startInstant, Instant endInstant, Instant registrationStartInstant, Instant registrationEndInstant, Bitmap eventPoster, Boolean geolocationRequired) throws Exception {
        super(name, startInstant, endInstant, registrationStartInstant, registrationEndInstant, eventPoster, geolocationRequired);
    }

    public EventMock(String name, Instant startInstant, Instant endInstant, Instant registrationStartInstant, Instant registrationEndInstant, Bitmap eventPoster, Integer capacity, Boolean geolocationRequired) throws Exception {
        super(name, startInstant, endInstant, registrationStartInstant, registrationEndInstant, eventPoster, capacity, geolocationRequired);
    }

    @Override
    public void setEventPoster(Bitmap eventPoster) throws Exception {

    }
}
