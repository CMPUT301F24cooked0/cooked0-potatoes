package com.example.myapplication.ui.facility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.Event;
import com.example.myapplication.Facility;
import com.example.myapplication.R;
import com.example.myapplication.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class CreateEventFragment extends Fragment {

    private Bitmap eventPoster;
    private EditText eventNameInput, eventStartInput, eventEndInput, eventCapacityInput, eventDetailsInput, eventRegStartInput, eventRegEndInput;
    private Switch geoRequiredSwitch;
    private Boolean geoRequired;
    private ImageView eventPosterInput;
    private Button createEventButton;
    private User user;
    private Facility facility;
    DatabaseManager databaseManager;
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
        eventRegStartInput = view.findViewById(R.id.regOpenInput);
        eventRegEndInput = view.findViewById(R.id.regEndInput);
        geoRequiredSwitch = view.findViewById(R.id.enableGeoSwitch);
        createEventButton = view.findViewById(R.id.createEventButton);
        databaseManager = new DatabaseManager();
        eventPoster = null;
        createEventButton.setOnClickListener(this::onCreateEventClick);

        // Get the user's facility
        facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        user = facilityViewModel.getOrganizer();
        if (user != null) {
            facility = user.getFacility();
        } else {
            Toast.makeText(getActivity(), "User is not an organizer", Toast.LENGTH_SHORT).show();
        }


        // Add image adding functionality for event poster
        ActivityResultLauncher<Intent> imagePickerLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            eventPoster = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(imageUri));
                            eventPosterInput.setImageBitmap(eventPoster);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        // Set click listener for event poster input
        eventPosterInput.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });



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
            String regStartDateTime = eventRegStartInput.getText().toString().trim();
            String regEndDateTime = eventRegEndInput.getText().toString().trim();
            String details = eventDetailsInput.getText().toString().trim();
            String capacityStr = eventCapacityInput.getText().toString().trim();
            geoRequired = geoRequiredSwitch.isChecked();

            // check if required fields are filled
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
            if (regStartDateTime.isEmpty()) {
                Toast.makeText(getActivity(), "Registration start date-time cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (regEndDateTime.isEmpty()) {
                Toast.makeText(getActivity(), "Registration end date-time cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if event capacity is valid
            Integer capacity = null;
            if (!capacityStr.isEmpty()) {
                try {
                    capacity = Integer.parseInt(capacityStr);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Capacity must be an integer", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (capacity <= 0) {
                    Toast.makeText(getActivity(), "Capacity must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // check if event details are empty
            if (details.isEmpty()) {
                details = null;
            }


            // Parse start and end date-time strings for Instant conversion
            Instant startInstant = parseDateTime(startDateTime);
            Instant endInstant = parseDateTime(endDateTime);

            // Check if start and end date-time are in valid format
            if (startInstant == null || endInstant == null) {
                Toast.makeText(getActivity(), "Invalid date-time format for event dates. Use DD/MM/YYYY HH:mm", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if start date-time is before end date-time
            if (startInstant.isAfter(endInstant)) {
                Toast.makeText(getActivity(), "Start time cannot be after end time", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse registration start and end date-time strings for Instant conversion
            Instant regStartInstant = parseDateTime(regStartDateTime);
            Instant regEndInstant = parseDateTime(regEndDateTime);

            // Check if registration start and end date-time are in valid format
            if (regStartInstant == null || regEndInstant == null) {
                Toast.makeText(getActivity(), "Invalid date-time format for registration dates. Use DD/MM/YYYY HH:mm", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if registration start date-time is before end date-time
            if (regStartInstant.isAfter(regEndInstant)) {
                Toast.makeText(getActivity(), "Registration start time cannot be after end time", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if event poster has been added
            if (eventPoster == null) {
                Toast.makeText(getActivity(), "Event poster cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the event
            Event event = new Event(name, details, startInstant, endInstant, regStartInstant, regEndInstant, eventPoster, geoRequired); // Create the event
            event.setCapacity(capacity);
            Boolean addedToDatabase = databaseManager.createEvent(facility, event); // Add the event to the database and set eventRef to the event object

            // Check if event was added to database
            if (!addedToDatabase) {
                Log.e("CreateEventFragment", "Error adding event to database");
                return;
            }
            // Set the QR code text of the event
            String qrID = UUID.randomUUID().toString(); // generate a random id for the QR code
            String eventPath = event.getEventReference().getPath(); // get the path of the event document
            String qrPath = eventPath + "/" + qrID; // concatenate the random id to the path
            event.getQrCode().setText(qrPath); // set the QR code text of the event to the path

            databaseManager.updateEvent(event); // update the event in the database now that the QR code text has been set

            facilityViewModel.setEventToManage(event); // set event to manage in FacilityViewModel

            // Add event to facility (if facility is available)
            if (facility != null) {
                facility.addEvent(event);
                Log.d("CreateEventFragment", "Event added to facility: " + facility.getName());
            } else {
                Toast.makeText(getActivity(), "Facility not found", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getActivity(), "Event created successfully!", Toast.LENGTH_SHORT).show();
            Log.d("CreateEventFragment", "Event created: " + event.getName());

            // Navigate to the download QR code page
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new DownloadQRFragment()).commit();



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
