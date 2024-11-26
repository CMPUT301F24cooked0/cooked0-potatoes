package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This fragment allows a user to view the details of an event after scanning a QR code.
 */
public class ViewEventDetailsFragment extends Fragment {
    View view;
    String eventPath;
    FirebaseFirestore db;
    DocumentReference eventRef;
    ImageView eventPoster;
    TextView eventName;
    TextView eventDesc;
    TextView eventTime;
    TextView eventDate;
    TextView registerStart;
    TextView registerEnd;
    TextView geolocation;
    Button joinWaitlistBtn;



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
        if (getArguments() == null) {
            Toast.makeText(requireContext(), "Unable to get event details", Toast.LENGTH_LONG).show();
        }
        eventPath = (String) getArguments().get("eventPath");
        db = FirebaseFirestore.getInstance();
        eventRef = db.document(eventPath);
        eventPoster = view.findViewById(R.id.event_poster_placeholder);
        eventName = view.findViewById(R.id.event_name_placeholder);
        eventDesc = view.findViewById(R.id.event_desc_placeholder);
        eventTime = view.findViewById(R.id.event_time_placeholder);
        eventDate = view.findViewById(R.id.event_date_placeholder);
        registerStart = view.findViewById(R.id.register_start_placeholder);
        registerEnd = view.findViewById(R.id.register_end_placeholder);
        geolocation = view.findViewById(R.id.geolocation_notice_placeholder);
        joinWaitlistBtn = view.findViewById(R.id.join_waitlist_button);
        // loadEventDetails();




    }
//    public void loadEventDetails() {
//        this.eventRef.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot.exists()) {
//                // TODO load event details from database
//
//            } else {
//                Toast.makeText(requireContext(), "Could not find event in database", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

}