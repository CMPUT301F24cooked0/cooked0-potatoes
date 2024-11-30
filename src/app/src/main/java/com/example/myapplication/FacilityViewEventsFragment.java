package com.example.myapplication;

import android.content.Intent;
import android.net.ipsec.ike.TunnelModeChildSessionParams;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This fragment is used to view the events of a facility and provide the option to edit the facility.
 */
public class FacilityViewEventsFragment extends Fragment {
    View view;
    TextView facilityName;
    TextView facilityLink;
    ListView eventList;
    EventArrayAdapter eventAdapter;
    Facility facility;
    FloatingActionButton addEventButton;

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
        // TODO get facility object
//        facility = (Facility) getIntent().getSerializableExtra("facility");
        facilityName = view.findViewById(R.id.facilityNamePlaceholder);
        facilityLink = view.findViewById(R.id.editFacilityLink);
        eventList = view.findViewById(R.id.eventList);
        addEventButton = view.findViewById(R.id.addBtn);
        eventAdapter = new EventArrayAdapter(requireContext(), facility.getEvents());
        eventList.setAdapter(eventAdapter);
        facilityName.setText(facility.getName());
        addEventButton.setOnClickListener(this::onClickAddEvent);
//        facilityLink.setOnClickListener(this::onClickEditFacility);
        eventList.setOnItemClickListener((parent, v, position, id) -> {
            // TODO Link with event managing page once navigation (and event managing page) is completed
//            FragmentManager fragmentManager = getParentFragmentManager();
//            EditEventFragment editEventFragment = new EditEventFragment();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("event", facility.getEvents().get(position));
//            editEventFragment.setArguments(bundle);
//            fragmentTransaction.replace(R.id.fragment_container, editEventFragment); // TODO have this redirect to event managing page first
//            fragmentTransaction.commit();

        });

    }
    public void onClickAddEvent(View view) {
        FragmentManager fragmentManager = getParentFragmentManager();
        CreateEventFragment createEventFragment = new CreateEventFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // TODO: pass in facility object once navigation completed
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("facility", facility);
//        createEventFragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.fragment_container, createEventFragment);
        fragmentTransaction.commit();

    }
    // TODO link with edit facility page once navigation completed
//    public void onClickEditFacility(View view) {
//        FragmentManager fragmentManager = getParentFragmentManager();
//        EditFacilityFragment editFacilityFragment = new EditFacilityFragment();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("facility", facility);
//        editFacilityFragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.fragment_container, editFacilityFragment);
//        fragmentTransaction.commit();
//
//
//    }

}