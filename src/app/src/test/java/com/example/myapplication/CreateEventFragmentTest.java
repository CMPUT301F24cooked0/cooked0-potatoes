package com.example.myapplication;

import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.widget.EditText;

import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

public class CreateEventFragmentTest {

    @Before
    public void setUp() {
        // No initialization required; FragmentScenario will handle the lifecycle
    }

    @Test
    public void testEventCreation() {
        // Launch the CreateEventFragment in a testing scenario
        FragmentScenario<CreateEventFragment> scenario = FragmentScenario.launchInContainer(CreateEventFragment.class);

        scenario.onFragment(fragment -> {
            // Access views and simulate entering data
            EditText eventNameInput = fragment.getView().findViewById(R.id.eventNameInput);
            EditText eventStartInput = fragment.getView().findViewById(R.id.eventStartInput);
            EditText eventEndInput = fragment.getView().findViewById(R.id.eventEndInput);
            EditText eventCapacityInput = fragment.getView().findViewById(R.id.eventCapInput);
            EditText eventDetailsInput = fragment.getView().findViewById(R.id.eventDetInput);

            eventNameInput.setText("Test Event");
            eventStartInput.setText("01/01/2025");
            eventEndInput.setText("02/01/2025");
            eventCapacityInput.setText("100");
            eventDetailsInput.setText("This is a test event.");

            // Set the event poster bitmap directly
            fragment.eventPoster = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

            // Call the method to retrieve user-entered data and create an event
            Event event = fragment.getUserEnteredEvent();

            // Assertions to validate the created event
            assertNotNull("Event should not be null", event);
            assertEquals("Event name should match", "Test Event", event.getName());
            assertEquals("Event capacity should match", 100, event.getCapacity().intValue());
            assertEquals("Event details should match", "This is a test event.", event.getDetails());
            assertNotNull("Event poster should not be null", event.getEventPoster());

            // Validate the event's start date
            Instant expectedStart = fragment.getParsedDate("01/01/2025").atStartOfDay().toInstant(fragment.getDefaultZoneOffset());
            assertEquals("Event start date should match", expectedStart, event.getInstant());
        });
    }

    @Test
    public void testEventCreationWithInvalidData() {
        FragmentScenario<CreateEventFragment> scenario = FragmentScenario.launchInContainer(CreateEventFragment.class);

        scenario.onFragment(fragment -> {
            // Access views and simulate entering invalid data
            EditText eventNameInput = fragment.getView().findViewById(R.id.eventNameInput);
            EditText eventStartInput = fragment.getView().findViewById(R.id.eventStartInput);
            EditText eventEndInput = fragment.getView().findViewById(R.id.eventEndInput);
            EditText eventCapacityInput = fragment.getView().findViewById(R.id.eventCapInput);
            EditText eventDetailsInput = fragment.getView().findViewById(R.id.eventDetInput);

            eventNameInput.setText("");
            eventStartInput.setText("invalid_date");
            eventEndInput.setText("02/01/2025");
            eventCapacityInput.setText("-10");
            eventDetailsInput.setText(null);

            // Don't set the event poster
            fragment.eventPoster = null;

            // Call the method to retrieve user-entered data and create an event
            Event event = fragment.getUserEnteredEvent();

            // Assertions to validate that no event is created
            assertNull("Event should be null for invalid input", event);
        });
    }
}
