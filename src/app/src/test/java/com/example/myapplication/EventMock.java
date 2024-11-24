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

    public EventMock(String name, Instant instant, Bitmap eventPoster) throws Exception {
        super(name, instant, eventPoster);
    }

    public EventMock(String name, Instant instant, Bitmap eventPoster, Integer capacity) throws Exception {
        super(name, instant, eventPoster, capacity);
    }

    @Override
    public void setEventPoster(Bitmap eventPoster) throws Exception {

    }
}
