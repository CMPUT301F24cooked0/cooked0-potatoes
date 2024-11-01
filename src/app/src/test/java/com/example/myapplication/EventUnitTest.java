package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Use EventMock instead of Event for testing, as it removes the
 * eventPoster requirement. See note on EventMock class for more info
 */
public class EventUnitTest {
    @Test
    public void sampleTest() {
        assertEquals(4, 2 + 2);
    }
}
