package com.example.myapplication.ui.facility;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.Facility;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * This class represents the facility creation page.
 */

public class FacilityCreationFragment extends Fragment {
    // TODO get user from bundle
    View view;
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button createFacilityButton;
    User facilityOwner;
    DatabaseManager databaseManager;
    String facilityName;
    String facilityAddressStr;
    Facility facility;
    FacilityViewModel facilityViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_facility_creation, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        facilityOwner = facilityViewModel.getOrganizer();
        facilityNameInput = view.findViewById(R.id.addFacilityName);
        facilityAddressInput = view.findViewById(R.id.addFacilityAddress);
        createFacilityButton = view.findViewById(R.id.createFacilityButton);
        databaseManager = new DatabaseManager();
        createFacilityButton.setOnClickListener(this::onClick);
    }
    public void onClick(View view) {
        // get facility name and address from input fields
        facilityName = facilityNameInput.getText().toString();
        facilityAddressStr = facilityAddressInput.getText().toString();
        if (facilityName.isEmpty() || facilityAddressStr.isEmpty()) {
            Toast.makeText(this.requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng facilityAddress = getAddress(facilityAddressStr); // convert address to LatLng
        if (facilityAddress == null) {
            Toast.makeText(this.requireContext(), "Invalid address", Toast.LENGTH_SHORT).show();
            return;
        }

        // create facility object
        try {
            facility = new Facility(facilityName, facilityAddress, facilityAddressStr);
        } catch (Exception e) {
            Toast.makeText(this.requireContext(), "Unable to create facility", Toast.LENGTH_SHORT).show();
            return;
        }

        facilityOwner.setFacility(facility); // set facility for user
        databaseManager.createFacility(facilityOwner, facility); // add facility to database and set facility document reference
    }

    public LatLng getAddress(String address) {
        // converts string address given by user to LatLng
        Geocoder geocoder = new Geocoder(this.requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
