package com.example.myapplication.ui.facility;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Event;
import com.example.myapplication.R;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This fragment allows an organizer to view, edit, and manage an event. It allows the organizer to
 * download a QR code that can be used to sign up for the event.
 */
public class ManageEventFragment extends Fragment {
    View view;
    private FacilityViewModel facilityViewModel;
    private Event event;
    private TextView eventName;
    private TextView eventDesc;
    private TextView eventDate;
    private TextView registerStart;
    private TextView registerEnd;
    private TextView downloadQRLink;
    private TextView editEventLink;
    private TextView eventCapacity;
    private ImageView eventPosterImageView;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manage_event, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize variables
        facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        event = facilityViewModel.getEventToManage();
        eventName = view.findViewById(R.id.event_name_placeholder);
        eventDesc = view.findViewById(R.id.event_desc_placeholder);
        eventDate = view.findViewById(R.id.event_date_placeholder);
        registerStart = view.findViewById(R.id.register_start_placeholder);
        registerEnd = view.findViewById(R.id.register_end_placeholder);
        downloadQRLink = view.findViewById(R.id.download_QR_link);
        editEventLink = view.findViewById(R.id.edit_event_link);
        eventCapacity = view.findViewById(R.id.event_capacity_placeholder);
        eventPosterImageView = view.findViewById(R.id.event_poster_placeholder);

        // Set text for event details
        // TODO update when new fields are added in event class
        eventPosterImageView.setImageBitmap(event.getEventPoster());
        eventName.setText(event.getName());
        //eventDate.setText(event.getInstant() + "- end"); // TODO add start and end
        String formattedStartDateTime = formatDateTime(event.getStartInstant());
        String formattedEndDateTime = formatDateTime(event.getEndInstant());
        String formattedRegStartDateTime = formatDateTime(event.getRegistrationStartInstant());
        String formattedRegEndDateTime = formatDateTime(event.getRegistrationEndInstant());
        if (formattedStartDateTime != null && formattedEndDateTime != null && formattedRegStartDateTime != null && formattedRegEndDateTime != null){
            eventDate.setText(formattedStartDateTime + " - " + formattedEndDateTime);
            registerStart.setText(formattedRegStartDateTime);
            registerEnd.setText(formattedRegEndDateTime);
        } else {
            Toast.makeText(getActivity(), "Could not format Instants", Toast.LENGTH_SHORT).show();
        }
        eventDesc.setText(event.getDescription() != null ? event.getDescription() : "");
        downloadQRLink.setOnClickListener(this::onClickDownloadQR);
        editEventLink.setOnClickListener(this::onClickEditEvent);
        String capacityStr = event.getCapacity() != null ? String.valueOf(event.getCapacity()) : "Unlimited";
        eventCapacity.setText("Spots Created -- " + capacityStr);


    }
    public void onClickDownloadQR (View view) {
        // Navigate to the download QR code page
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new DownloadQRFragment()).commit();

    }

    public void onClickEditEvent (View view) {
        // Navigate to edit event page
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new EditEventFragment()).commit();
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