package com.example.myapplication.ui.facility;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.CreateEventFragment;
import com.example.myapplication.EditEventFragment;
import com.example.myapplication.EventArrayAdapter;
import com.example.myapplication.Facility;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This fragment is used to view the events of a facility and provide the option to edit the facility.
 */
public class FacilityViewEventsFragment extends Fragment {
    View view;
    User user;
    TextView facilityName;
    TextView facilityLink;
    ListView eventList;
    EventArrayAdapter eventAdapter;
    Facility facility;
    FloatingActionButton addEventButton;
    FacilityViewModel facilityViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_facility_view_events, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize variables
        facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        user = facilityViewModel.getOrganizer();
        facility = user.getFacility();
        facilityName = view.findViewById(R.id.facilityNamePlaceholder);
        facilityLink = view.findViewById(R.id.editFacilityLink);
        eventList = view.findViewById(R.id.eventList);
        addEventButton = view.findViewById(R.id.addBtn);
        eventAdapter = new EventArrayAdapter(this.requireContext(), facilityViewModel.getEvents().getValue());
        eventList.setAdapter(eventAdapter);
        facilityViewModel.setEvents();
        facilityViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            eventAdapter.clear();
            eventAdapter.addAll(events);
            eventAdapter.notifyDataSetChanged();
        });

        // Set click listeners for buttons
        addEventButton.setOnClickListener(this::onClickAddEvent);
        facilityLink.setOnClickListener(this::onClickEditFacility);

        // Set click listener for event list items
        eventList.setOnItemClickListener((parent, v, position, id) -> {
            // TODO Link with event managing page once navigation (and event managing page) is completed
            FragmentManager fragmentManager = getParentFragmentManager();
            EditEventFragment editEventFragment = new EditEventFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("eventIndex", position);
            editEventFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, editEventFragment); // TODO have this redirect to event managing page first
            fragmentTransaction.commit();

        });

    }
    // Navigate to create event page
    public void onClickAddEvent(View view) {
        FragmentManager fragmentManager = getParentFragmentManager();
        CreateEventFragment createEventFragment = new CreateEventFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, createEventFragment);
        fragmentTransaction.commit();

    }
    // Navigate to edit facility page
    public void onClickEditFacility(View view) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FacilityEditFragment facilityEditFragment = new FacilityEditFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, facilityEditFragment);
        fragmentTransaction.commit();


    }

}