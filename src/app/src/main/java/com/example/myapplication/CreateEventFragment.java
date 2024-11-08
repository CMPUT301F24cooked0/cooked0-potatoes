package com.example.myapplication;

import static android.media.MediaSyncEvent.createEvent;

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


import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreateEventFragment extends Fragment {
    private EditText eventName, eventDate, eventCapacity, eventDetails, eventStart, eventEnd;
    private Button createEventButton;
    private ImageView eventPoster;
    private Switch geoLocation;
    private Bitmap selectedPosterBitmap;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event, container, false);



        eventName = view.findViewById(R.id.eventNameInput);
        eventCapacity = view.findViewById(R.id.eventCapInput);
        eventDetails = view.findViewById(R.id.eventDetInput);
        eventStart = view.findViewById(R.id.eventStartInput);
        eventEnd =view.findViewById(R.id.eventEndInput);
        eventPoster = view.findViewById(R.id.eventPosterPlaceholder);
        // TODO get facility from previous activity


        createEventButton = view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(this::onClick);

        return view;

    }

    public Event createEvent(String name, String startDate, String endDate, int capacity, String details, int facilityId, Bitmap eventPoster) {
        if (name.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || details.isEmpty()) {
            return null;  // Return null if fields are invalid
        }

        try {
            // Parse the start and end date strings into Date objects
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            // Ensure the dates are valid
            if (start == null || end == null) {
                return null;  // Return null if parsing failed
            }

            // Create Event object
            return new Event(name, start, eventPoster, capacity);


        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null if there was an exception
        }
    }

    private void onClick(View v) {

        String name = eventName.getText().toString().trim();
        Integer capacity = Integer.valueOf(eventCapacity.getText().toString().trim());
        String details = eventDetails.getText().toString().trim();
        String start = eventStart.getText().toString().trim();
        String end = eventEnd.getText().toString().trim();
        int facilityId = getArguments().getInt("facilityId", -1);  // Default value is -1 in case facilityId is not found
        //boolean isGeoLocationEnabled = geoLocation.isChecked();
        Bitmap eventPosterBitmap = ((BitmapDrawable) eventPoster.getDrawable()).getBitmap();

        Event event = createEvent(name, start, end, capacity, details, facilityId, eventPoster.getDrawingCache());
        // TODO update facility array with event

        if (event != null) {
            // For now, just log the event or handle further operations
            Log.d("CreateEvent", "Event created: " + event.getName());
        } else {
            Toast.makeText(getActivity(), "Invalid event data", Toast.LENGTH_SHORT).show();
        }
    }
}
