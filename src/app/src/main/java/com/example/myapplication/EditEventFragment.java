package com.example.myapplication;

import android.app.DatePickerDialog;
import androidx.fragment.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * This fragment is used to edit an existing event.
 */

public class EditEventFragment extends Fragment {

    private EditText eventNameEditText, eventCapacityEditText;
    private ImageView eventPosterImageView;
    private Button saveButton, dateButton;
    private Date eventDate;
    private Bitmap eventPoster;
    private Event event;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        eventNameEditText = view.findViewById(R.id.editEventNameInput);
        eventCapacityEditText = view.findViewById(R.id.editEventCapInput);
        // TODO get event from previous activity
        //dateButton = view.findViewById(R.id.editEventStartInput);
        saveButton = view.findViewById(R.id.editEventButton);

        // Pre-fill fields with existing event data
        eventNameEditText.setText(event.getName());
        eventCapacityEditText.setText(event.getCapacity() != null ? String.valueOf(event.getCapacity()) : "");
        eventPosterImageView.setImageBitmap(event.getEventPoster());
        eventDate = event.getDate();

        // Set up date button to show a date picker
        dateButton.setOnClickListener(v -> showDatePickerDialog());

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

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(eventDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    eventDate = selectedDate.getTime();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void saveEvent() throws Exception {
        String name = eventNameEditText.getText().toString().trim();
        String capacityStr = eventCapacityEditText.getText().toString().trim();
        Integer capacity = TextUtils.isEmpty(capacityStr) ? null : Integer.parseInt(capacityStr);

        // Apply updates using Event class validation methods
        event.setName(name);
        event.setDate(eventDate);
        event.setCapacity(capacity);

        // Set the poster (update this as per your logic for changing the poster, e.g., via an image picker)
        event.setEventPoster(eventPoster);
    }
}