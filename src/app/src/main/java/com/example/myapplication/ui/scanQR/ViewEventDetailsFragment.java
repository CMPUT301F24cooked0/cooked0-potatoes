package com.example.myapplication.ui.scanQR;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.EntrantStatus;
import com.example.myapplication.Event;
import com.example.myapplication.R;
import com.example.myapplication.Status;
import com.example.myapplication.User;
import com.google.android.gms.maps.model.LatLng;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This fragment allows a user to view the details of an event after scanning a QR code.
 */
public class ViewEventDetailsFragment extends Fragment {
    View view;
    User user;
    Event eventToView;
    ImageView eventPoster;
    TextView eventName;
    TextView eventDesc;
    TextView eventDate;
    TextView registerStart;
    TextView registerEnd;
    TextView geolocation;
    Button joinWaitlistBtn;
    ScanQRViewModel scanQRViewModel;
    DatabaseManager databaseManager;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.getDefault());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_event_details, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanQRViewModel = new ViewModelProvider(requireActivity()).get(ScanQRViewModel.class);
        eventPoster = view.findViewById(R.id.event_poster_placeholder);
        eventName = view.findViewById(R.id.event_name_placeholder);
        eventDesc = view.findViewById(R.id.event_desc_placeholder);
        eventDate = view.findViewById(R.id.event_date_placeholder);
        registerStart = view.findViewById(R.id.register_start_placeholder);
        registerEnd = view.findViewById(R.id.register_end_placeholder);
        geolocation = view.findViewById(R.id.download_QR_link);
        joinWaitlistBtn = view.findViewById(R.id.join_waitlist_button);
        eventToView = scanQRViewModel.getEventToView();
        user = scanQRViewModel.getUser();
        databaseManager = new DatabaseManager();

        eventName.setText(eventToView.getName()); // set event name
        // set event date
        // TODO add register start and end dates, geolocation, description
        String eventStartStr = formatDateTime(eventToView.getStartInstant());
        String eventEndStr = formatDateTime(eventToView.getEndInstant());
        String eventRegStartStr = formatDateTime(eventToView.getRegistrationStartInstant());
        String eventRegEndStr = formatDateTime(eventToView.getRegistrationEndInstant());
        if (eventStartStr != null && eventEndStr != null && eventRegStartStr != null && eventRegEndStr != null) {
            eventDate.setText(eventStartStr + " - " + eventEndStr);
            registerStart.setText(eventRegStartStr);
            registerEnd.setText(eventRegEndStr);

        }
        eventDesc.setText(eventToView.getDescription() != null ? eventToView.getDescription() : "");

        // Set event poster
        Bitmap eventPosterBitmap = eventToView.getEventPoster();
        eventPoster.setImageBitmap(eventPosterBitmap);

        // Set geolocation notice
        if (eventToView.getGeolocationRequired()) {
            geolocation.setText("This event requires geolocation to join");
            Toast.makeText(requireActivity(), "Warning: Geolocation required to join event", Toast.LENGTH_SHORT).show();
        }


        joinWaitlistBtn.setOnClickListener(this::onClickJoinWaitlist);
        // TODO add button to leave?

    }

    /**
     * Called when the user clicks the join waitlist button.
     * @param view
     */
    public void onClickJoinWaitlist(View view) {
        if (user != null) {
            LatLng userLocation = null; // user location
            Status userStatus = Status.none;
            EntrantStatus newEntrant = new EntrantStatus(user, userLocation, userStatus);
            databaseManager.createEntrantStatus(eventToView, newEntrant); // add entrant to database
        }
    }

    /**
     * Formats an Instant into a string in "dd/MM/yyyy HH:mm" format.
     *
     * @param instant the Instant to format.
     * @return the formatted string.
     */
    private String formatDateTime(Instant instant) {
        try {
            return instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(dateFormatter);
        } catch (Exception e) {
            return null;
        }
    }

}