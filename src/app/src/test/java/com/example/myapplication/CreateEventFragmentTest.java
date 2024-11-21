package com.example.myapplication;

import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;

public class CreateEventFragmentTest {

    @Before
    public void setUp() {
        // No need to initialize FragmentActivity since FragmentScenario will handle that
    }

    @Test
    public void testEventCreation() {
        // Launch the CreateEventFragment
        FragmentScenario<CreateEventFragment> scenario = FragmentScenario.launchInContainer(CreateEventFragment.class);

        scenario.onFragment(fragment -> {
            // Simulate entering data into the fields
            EditText eventNameInput = fragment.getView().findViewById(R.id.eventNameInput);
            EditText eventStartInput = fragment.getView().findViewById(R.id.eventStartInput);
            EditText eventEndInput = fragment.getView().findViewById(R.id.eventEndInput);
            EditText eventCapacityInput = fragment.getView().findViewById(R.id.eventCapInput);
            EditText eventDetailsInput = fragment.getView().findViewById(R.id.eventDetInput);

            eventNameInput.setText("Test Event");
            eventStartInput.setText("01/01/2025");
            eventEndInput.setText("02/01/2025");
            eventCapacityInput.setText("100");
            eventDetailsInput.setText("Test event description");

            Button createButton = fragment.getView().findViewById(R.id.createEventButton);
            createButton.performClick();

            // Check if the event was created successfully
            Event event = fragment.createEvent(
                    "Test Event", "01/01/2025", "02/01/2025", 100, "Test event description", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            );

            assertNotNull(event);
            assertEquals("Test Event", event.getName());
            assertEquals(Optional.of(100), event.getCapacity());
            assertNotNull(event.getDate());
        });
    }
}
