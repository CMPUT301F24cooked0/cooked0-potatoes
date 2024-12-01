package com.example.myapplication.ui.facility;

import android.app.DatePickerDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Event;
import com.example.myapplication.R;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This fragment is used to edit an existing event.
 */

public class EditEventFragment extends Fragment {

    private EditText eventNameEditText, eventCapacityEditText, eventStartEditText, eventEndEditText, eventDetailsEditText;
    private ImageView eventPosterImageView;
    private Button saveButton;
    private Bitmap eventPoster;
    private Event event;
    private FacilityViewModel facilityViewModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.getDefault());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        // Initialize views and variables
        facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        event = facilityViewModel.getEventToManage();
        eventNameEditText = view.findViewById(R.id.editEventNameInput);
        eventCapacityEditText = view.findViewById(R.id.editEventCapInput);
        eventStartEditText = view.findViewById(R.id.editEventStartInput);
        eventEndEditText = view.findViewById(R.id.editEventEndInput);
        eventDetailsEditText = view.findViewById(R.id.editEventDetInput);
        eventPosterImageView = view.findViewById(R.id.eventPosterPlaceholder);
        eventPoster = event.getEventPoster();
        saveButton = view.findViewById(R.id.editEventButton);



        // Pre-fill fields with existing event data
        eventNameEditText.setText(event.getName());
        eventCapacityEditText.setText(event.getCapacity() != null ? String.valueOf(event.getCapacity()) : "");
        eventStartEditText.setText(event.getInstant().toString());
        eventPosterImageView.setImageBitmap(eventPoster);

        // add image adding functionality (update eventposter and imageview)


        // Save button to validate and save updated event details
        saveButton.setOnClickListener(v -> {
            try {
                saveEvent();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private void saveEvent() throws Exception {
        String name = eventNameEditText.getText().toString().trim();
        String capacityStr = eventCapacityEditText.getText().toString().trim();
        String startDateTime = eventStartEditText.getText().toString().trim();
        String endDateTime = eventEndEditText.getText().toString().trim();
        Integer capacity = TextUtils.isEmpty(capacityStr) ? null : Integer.parseInt(capacityStr);

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

        // Apply updates using Event class validation methods
        event.setName(name);
        event.setInstant(startInstant);
        event.setCapacity(capacity);
        event.setEventPoster(eventPoster);

        // TODO update the event in the database

        // update the event in the ViewModel
        facilityViewModel.setEvents();
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
            return null;
        }
    }
}
