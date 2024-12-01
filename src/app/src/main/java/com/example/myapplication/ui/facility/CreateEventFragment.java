package com.example.myapplication.ui.facility;

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
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Event;
import com.example.myapplication.Facility;
import com.example.myapplication.R;
import com.example.myapplication.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CreateEventFragment extends Fragment {

    private Bitmap eventPoster;
    private EditText eventNameInput, eventStartInput, eventEndInput, eventCapacityInput, eventDetailsInput;
    private ImageView eventPosterInput;
    private Button createEventButton;
    private User user;
    private Facility facility; // Facility object representing the facility this event belongs to
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.getDefault());
    private FacilityViewModel facilityViewModel;

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
        eventPoster = null;
        createEventButton.setOnClickListener(this::onCreateEventClick);

        // Get the user's facility
        facilityViewModel = new FacilityViewModel();
        user = facilityViewModel.getOrganizer();
        if (user != null) {
            facility = user.getFacility();
        } else {
            Toast.makeText(getActivity(), "User is not an organizer", Toast.LENGTH_SHORT).show();
        }

        // add image adding functionality (update eventposter and imageview)



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
            // Bitmap poster = ((BitmapDrawable) eventPosterInput.getDrawable()).getBitmap();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Event name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startDateTime.isEmpty()) {
                Toast.makeText(getActivity(), "Start date-time cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endDateTime.isEmpty()) {
                Toast.makeText(getActivity(), "End date-time cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }


            Integer capacity = null;
            if (!eventCapacityInput.getText().toString().isEmpty()) {
                capacity = Integer.parseInt(eventCapacityInput.getText().toString().trim());
            }


            // Parse the dates

            Instant startInstant = parseDateTime(startDateTime);
            Instant endInstant = parseDateTime(endDateTime);
            if (startInstant == null || endInstant == null) {
                Toast.makeText(getActivity(), "Invalid date-time format. Use DD/MM/YYYY HH:mm", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startInstant.isAfter(endInstant)) {
                Toast.makeText(getActivity(), "Start time cannot be after end time", Toast.LENGTH_SHORT).show();
                return;
            }

            if (eventPoster == null) {
                Toast.makeText(getActivity(), "Event poster cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the event
            Event event = new Event(name, startInstant, eventPoster, capacity);
            // TODO add event to database
            event.getQrCode().setText("test"); // FIXME change this to the actual path
            //event.getQrCode().setText(event.getEventReference().getpath()); // once event is added to database, this will be set

            // Add event to facility (if facility is available)
            if (facility != null) {
                facility.addEvent(event);
                facilityViewModel.setEvents(); // Update the events in the ViewModel
                facilityViewModel.setEventToManage(event);
                Log.d("CreateEventFragment", "Event added to facility: " + facility.getName());
            }

            Toast.makeText(getActivity(), "Event created successfully!", Toast.LENGTH_SHORT).show();
            Log.d("CreateEventFragment", "Event created: " + event.getName());
            facilityViewModel.setEvents(); // Update the events in the ViewModel


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
    private Instant parseDateTime(String dateTimeStr) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, dateFormatter);
            return dateTime.atZone(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            return null;
        }
    }
}
