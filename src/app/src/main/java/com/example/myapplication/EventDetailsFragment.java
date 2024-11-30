package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class EventDetailsFragment extends Fragment {
    private TextView eventNameTextView, eventDayTextView, eventTimeTextView, eventDateTextView,
            registrationOpensTextView, registerByTextView, spotsCreatedTextView, waitingListCountTextView;
    private ImageView eventPosterImageView;
    private Button editEventButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout
        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        // Initialize views with correct IDs from XML
        eventPosterImageView = rootView.findViewById(R.id.eventPoster);
        eventNameTextView = rootView.findViewById(R.id.eventName);
        eventDayTextView = rootView.findViewById(R.id.eventDay);
        eventTimeTextView = rootView.findViewById(R.id.eventTime);
        eventDateTextView = rootView.findViewById(R.id.eventDate);
        registrationOpensTextView = rootView.findViewById(R.id.registrationOpens);
        registerByTextView = rootView.findViewById(R.id.registerBy);
        spotsCreatedTextView = rootView.findViewById(R.id.spotsCreated);
        waitingListCountTextView = rootView.findViewById(R.id.waitingListCount);
        editEventButton = rootView.findViewById(R.id.editEventButton);

        // Assuming you have passed the event data through the bundle
        if (getArguments() != null) {
            String eventName = getArguments().getString("eventName");
            String eventDay = getArguments().getString("eventDay");
            String eventTime = getArguments().getString("eventTime");
            String eventDate = getArguments().getString("eventDate");
            String registrationOpens = getArguments().getString("registrationOpens");
            String registerBy = getArguments().getString("registerBy");
            int spotsCreated = getArguments().getInt("spotsCreated");
            String waitingList = getArguments().getString("waitingList");
            Bitmap eventPoster = getArguments().getParcelable("eventPoster");

            // Set the data to the views
            eventNameTextView.setText(eventName);
            eventDayTextView.setText(eventDay);
            eventTimeTextView.setText(eventTime);
            eventDateTextView.setText(eventDate);
            registrationOpensTextView.setText("Registration opens: " + registrationOpens);
            registerByTextView.setText("Register by: " + registerBy);
            spotsCreatedTextView.setText("Spots Created: " + spotsCreated);
            waitingListCountTextView.setText("Waiting List: " + waitingList);

            if (eventPoster != null) {
                eventPosterImageView.setImageBitmap(eventPoster);
            }
        }

        return rootView;
    }
}
