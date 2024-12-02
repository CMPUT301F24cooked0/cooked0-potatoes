package com.example.myapplication.ui.facility;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Event;
import com.example.myapplication.R;

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
        downloadQRLink.setOnClickListener(this::onClickDownloadQR);
        editEventLink.setOnClickListener(this::onClickEditEvent);
        eventCapacity.setText("Spots Created -- " + event.getCapacity());


        Button waitingListButton = view.findViewById(R.id.participants_waiting_button);
        waitingListButton.setOnClickListener(v -> {
            // Pass only the event capacity to WaitingListFragment
            Bundle bundle = new Bundle();
            bundle.putInt("event_capacity", event.getCapacity());  // Pass the event capacity

            // Navigate to WaitingListFragment with the bundle
            NavController navController = NavHostFragment.findNavController(ManageEventFragment.this);
            navController.navigate(R.id.navigation_participants, bundle);  // Pass the bundle with capacity
        });

    }

    public void onClickWaitingList() {
        // Create an instance of the WaitingListFragment
        ViewParticipantsLists viewParticipantsLists = new ViewParticipantsLists();

        // Replace the current fragment with WaitingListFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, viewParticipantsLists); // Use the container ID that holds the fragments
        transaction.addToBackStack(null); // Allow users to navigate back to the previous fragment
        transaction.commit();
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

}