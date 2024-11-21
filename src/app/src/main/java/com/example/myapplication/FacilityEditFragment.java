package com.example.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * This class represents a page for editing an existing facility.
 */
public class FacilityEditFragment extends Fragment {
    // TODO get facility from bundle
    View view;
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button editButton;
    Facility existingFacility;
    DatabaseManager databaseManager;
    String addressStr;
    String facilityName;
    String facilityAddressStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_facility_edit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        facilityNameInput = view.findViewById(R.id.editFacilityName);
        facilityAddressInput = view.findViewById(R.id.editFacilityAddress);
        editButton = view.findViewById(R.id.editFacilityButton);
        databaseManager = new DatabaseManager();
        //existingFacility = (Facility) getArguments().getSerializable("facility");
        facilityNameInput.setText(existingFacility.getName()); // autofill existing facility name
        addressStr = latLngToAddress(existingFacility.getLocation());// convert LatLng to address
        if (addressStr == null) {
            Toast.makeText(this.requireContext(), "Error converting to string address", Toast.LENGTH_SHORT).show();
            return;
        }
        facilityAddressInput.setText(addressStr); // autofill existing facility address
        editButton.setOnClickListener(this::onClick);

    }
    public void onClick(View view) {
        // get facility name and address from input fields
        facilityName = facilityNameInput.getText().toString();
        facilityAddressStr = facilityAddressInput.getText().toString();
        if (facilityName.isEmpty() || facilityAddressStr.isEmpty()) {
            Toast.makeText(this.requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // convert address to LatLng
        LatLng facilityAddress = getAddress(facilityAddressStr);
        if (facilityAddress == null) {
            Toast.makeText(this.requireContext(), "Invalid address", Toast.LENGTH_SHORT).show();
            return;
        }
        existingFacility.setName(facilityName);
        existingFacility.setLocation(facilityAddress);
        // update facility in database
        databaseManager.updateFacility(existingFacility);


    }

    public String latLngToAddress(LatLng location) {
        // converts LatLng input to string address
        Geocoder geocode = new Geocoder(this.requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocode.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}