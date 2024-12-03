package com.example.myapplication.ui.scanQR;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
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
    Button leaveWaitlistBtn;
    Button acceptBtn;
    Button declineBtn;
    EntrantStatus userEntrant;
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
        leaveWaitlistBtn = view.findViewById(R.id.leave_waitlist_button);
        acceptBtn = view.findViewById(R.id.accept_button);
        declineBtn = view.findViewById(R.id.decline_button);
        eventToView = scanQRViewModel.getEventToView();
        user = scanQRViewModel.getUser();
        databaseManager = new DatabaseManager();
        userEntrant = null;

        eventName.setText(eventToView.getName()); // set event name
        // set event date
        String eventStartStr = formatDateTime(eventToView.getStartInstant());
        String eventEndStr = formatDateTime(eventToView.getEndInstant());
        String eventRegStartStr = formatDateTime(eventToView.getRegistrationStartInstant());
        String eventRegEndStr = formatDateTime(eventToView.getRegistrationEndInstant());
        if (eventStartStr != null && eventEndStr != null && eventRegStartStr != null && eventRegEndStr != null) {
            eventDate.setText(eventStartStr + " - " + eventEndStr);
            registerStart.setText("Registration Opens: " + eventRegStartStr);
            registerEnd.setText("Registration Ends: " + eventRegEndStr);

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

        // Set onclick listeners for buttons
        joinWaitlistBtn.setOnClickListener(this::onClickJoinWaitlist);
        leaveWaitlistBtn.setOnClickListener(this::onClickLeaveWaitlist);
        acceptBtn.setOnClickListener(this::onClickAccept);
        declineBtn.setOnClickListener(this::onClickDecline);


        try {
            for (EntrantStatus entrant : eventToView.getEntrantStatuses()) {
                if (entrant.getEntrant().getUniqueID().equals(user.getUniqueID())) {
                    if (entrant.getStatus() == Status.none) {
                        userEntrant = entrant;
                        leaveWaitlistBtn.setVisibility(View.VISIBLE);
                        break;
                    } else if (entrant.getStatus() == Status.chosenAndPending) {
                        userEntrant = entrant;
                        acceptBtn.setVisibility(View.VISIBLE);
                        declineBtn.setVisibility(View.VISIBLE);
                        break;
                    } else if (entrant.getStatus() == Status.chosenAndAccepted) {
                        userEntrant = entrant;
                        Toast.makeText(requireActivity(), "You have been accepted to this event", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (entrant.getStatus() == Status.notChosen) {
                        userEntrant = entrant;
                        Toast.makeText(requireActivity(), "You were not chosen for this event. You may opt in again", Toast.LENGTH_SHORT).show();
                        joinWaitlistBtn.setVisibility(View.VISIBLE);
                        break;
                    } else if (entrant.getStatus() == Status.chosenAndDeclined) {
                        userEntrant = entrant;
                        Toast.makeText(requireActivity(), "You declined to join this event", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("ViewEventDetailsFragment", "Error getting entrant statuses", e);
        }
        if (userEntrant == null) {
            joinWaitlistBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called when the user clicks the join waitlist button.
     * @param view
     */
    public void onClickJoinWaitlist(View view) {
        if (userEntrant == null) {
            LatLng userLocation = new LatLng(1, 1); // user location
            Status userStatus = Status.none;
            userEntrant = new EntrantStatus(user, userLocation, userStatus);
            databaseManager.createEntrantStatus(eventToView, userEntrant); // add entrant to database
            Toast.makeText(requireActivity(), "You have been added to the waitlist", Toast.LENGTH_SHORT).show();
            joinWaitlistBtn.setVisibility(View.GONE);
        } else if (userEntrant.getStatus() == Status.notChosen) {
            userEntrant.setStatus(Status.none);
            databaseManager.updateEntrantStatus(userEntrant);
        }
        joinWaitlistBtn.setVisibility(View.GONE);
    }

    /**
     * Called when the user clicks the leave waitlist button.
     * @param view
     */
    public void onClickLeaveWaitlist(View view) {
        if (userEntrant != null) {
            databaseManager.deleteEntrantStatus(userEntrant);
            Boolean isDeleted = databaseManager.deleteEntrantStatus(userEntrant);
            if (isDeleted) {
                Toast.makeText(requireActivity(), "You have been removed from the waitlist", Toast.LENGTH_SHORT).show();
            }
            leaveWaitlistBtn.setVisibility(View.GONE);
        }
        leaveWaitlistBtn.setVisibility(View.GONE);
    }

    /**
     * Called when the user clicks the accept button.
     * @param view
     */
    public void onClickAccept(View view) {
        if (userEntrant != null) {
            userEntrant.setStatus(Status.chosenAndAccepted);
            databaseManager.updateEntrantStatus(userEntrant);
        }
        Toast.makeText(requireActivity(), "You have accepted joining this event", Toast.LENGTH_SHORT).show();
        acceptBtn.setVisibility(View.GONE);
        declineBtn.setVisibility(View.GONE);

    }
    /**
     * Called when the user clicks the decline button.
     * @param view
     */
    public void onClickDecline(View view) {
        if (userEntrant != null) {
            userEntrant.setStatus(Status.chosenAndDeclined);
            databaseManager.updateEntrantStatus(userEntrant);
        }
        Toast.makeText(requireActivity(), "You have declined joining this event", Toast.LENGTH_SHORT).show();
        acceptBtn.setVisibility(View.GONE);
        declineBtn.setVisibility(View.GONE);

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