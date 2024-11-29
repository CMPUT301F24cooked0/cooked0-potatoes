package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CreateEventFragment extends Fragment {

    public Bitmap eventPoster;
    private EditText eventNameInput, eventStartInput, eventEndInput, eventCapacityInput, eventDetailsInput;
    private ImageView eventPosterInput;
    private Button createEventButton;
    private Facility facility; // Facility object representing the facility this event belongs to
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        // Initialize UI elements
        eventNameInput = view.findViewById(R.id.eventNameInput);
        eventStartInput = view.findViewById(R.id.eventStartInput);
        eventEndInput = view.findViewById(R.id.eventEndInput);
        eventCapacityInput = view.findViewById(R.id.eventCapInput);
        eventDetailsInput = view.findViewById(R.id.eventDetInput);
        eventPosterInput = view.findViewById(R.id.eventPosterPlaceholder);

        createEventButton = view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(this::onCreateEventClick);

        // Assume facility object is passed via arguments
        if (getArguments() != null) {
            facility = (Facility) getArguments().getSerializable("facility");
        }

        return view;
    }

    /**
     * Handles the Create Event button click.
     */
    private void onCreateEventClick(View v) {
        try {
            String name = eventNameInput.getText().toString().trim();
            String startDateTime = eventStartInput.getText().toString().trim();
            String endDateTime = eventEndInput.getText().toString().trim();
            String details = eventDetailsInput.getText().toString().trim();
            Bitmap poster = ((BitmapDrawable) eventPosterInput.getDrawable()).getBitmap();

            Integer capacity = null;
            if (!eventCapacityInput.getText().toString().isEmpty()) {
                capacity = Integer.parseInt(eventCapacityInput.getText().toString().trim());
            }

            // Parse the dates
            Instant startInstant = parseDateTime(startDateTime);
            Instant endInstant = parseDateTime(endDateTime);

            if (startInstant.isAfter(endInstant)) {
                Toast.makeText(getActivity(), "Start time cannot be after end time", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the event
            Event event = new Event(name, startInstant, poster, capacity);
            event.setDetails(details);

            // Add event to facility (if facility is available)
            if (facility != null) {
                facility.addEvent(event);
                Log.d("CreateEventFragment", "Event added to facility: " + facility.getName());
            }

            Toast.makeText(getActivity(), "Event created successfully!", Toast.LENGTH_SHORT).show();
            Log.d("CreateEventFragment", "Event created: " + event.getName());

        } catch (Exception e) {
            Log.e("CreateEventFragment", "Error creating event", e);
            Toast.makeText(getActivity(), "Error creating event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parses a date-time string into an Instant.
     *
     * @param dateTimeStr the date-time string in "dd/MM/yyyy HH:mm" format.
     * @return an Instant representing the parsed date-time.
     * @throws Exception if the input is invalid.
     */
    private Instant parseDateTime(String dateTimeStr) throws Exception {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, dateFormatter);
            return dateTime.atZone(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            throw new Exception("Invalid date-time format. Use dd/MM/yyyy HH:mm.");
        }
    }
}
