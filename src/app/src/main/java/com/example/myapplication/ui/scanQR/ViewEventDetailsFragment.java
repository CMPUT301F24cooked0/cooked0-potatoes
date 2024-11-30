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

import com.example.myapplication.Event;
import com.example.myapplication.R;

/**
 * This fragment allows a user to view the details of an event after scanning a QR code.
 */
public class ViewEventDetailsFragment extends Fragment {
    View view;
    Event eventToView;
    ImageView eventPoster;
    TextView eventName;
    TextView eventDesc;
    TextView eventTime;
    TextView eventDate;
    TextView registerStart;
    TextView registerEnd;
    TextView geolocation;
    Button joinWaitlistBtn;
    ScanQRViewModel scanQRViewModel;


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
        geolocation = view.findViewById(R.id.geolocation_notice_placeholder);
        joinWaitlistBtn = view.findViewById(R.id.join_waitlist_button);
        eventToView = scanQRViewModel.getEventToView();

        eventName.setText(eventToView.getName()); // set event name
        // set event date
        String eventStartStr = eventToView.getInstant().toString();
        // String eventEndStr = eventToView.getInstant().toString(); // TODO get end date when available in base class
        eventDate.setText(eventStartStr + " - end"); // TODO add end date
        // eventDesc.setText(eventToView.getDescription()); // TODO add description
        // registerStart.setText(eventToView.getInstant().toString()); // TODO add register start date
        // registerEnd.setText(eventToView.getInstant().toString()); // TODO add register end date

        // set event poster
        Bitmap eventPosterBitmap = eventToView.getEventPoster();
        eventPoster.setImageBitmap(eventPosterBitmap);
        // TODO add boolean check for geolocation
        // TODO add button to join/leave waitlist

    }
}