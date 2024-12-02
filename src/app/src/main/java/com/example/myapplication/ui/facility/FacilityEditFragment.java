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
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * This class represents a page for editing an existing facility.
 */
public class FacilityEditFragment extends Fragment {
    View view;
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button editButton;
    Facility existingFacility;
    DatabaseManager databaseManager;
    String addressStr;
    String facilityName;
    String facilityAddressStr;
    LatLng facilityLocation;

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
        FacilityViewModel facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        existingFacility = facilityViewModel.getOrganizer().getFacility();
        facilityNameInput.setText(existingFacility.getName()); // autofill existing facility name
        addressStr = existingFacility.getAddress(); // get existing facility address string
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

        // convert address to LatLng if it has changed otherwise use existing LatLng
        if (addressStr.equals(facilityAddressStr)) {
            facilityLocation = existingFacility.getLocation();
        } else {
            facilityLocation = getAddress(facilityAddressStr);
            if (facilityLocation == null) {
                Toast.makeText(this.requireContext(), "Invalid address", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // set facility name, address, and location in existing facility object
        try {
            existingFacility.setName(facilityName);
            existingFacility.setAddress(facilityAddressStr);
            existingFacility.setLocation(facilityLocation);
        } catch (Exception e) {
            Toast.makeText(this.requireContext(), "Unable to update facility", Toast.LENGTH_SHORT).show();
            return;
        }


        databaseManager.updateFacility(existingFacility); // update facility in database


    }

    /**
     * Converts a string address to a LatLng object.
     * @param address string address
     * @return LatLng object
     */
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