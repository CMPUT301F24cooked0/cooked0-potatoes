package com.example.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class FacilityCreationFragment extends AppCompatActivity {
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button createFacilityButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_creation);
        facilityNameInput = findViewById(R.id.addFacilityName);
        facilityAddressInput = findViewById(R.id.addFacilityAddress);
        createFacilityButton = findViewById(R.id.createFacilityButton);
        createFacilityButton.setOnClickListener(view -> {
            // get facility name and address from input fields
            String facilityName = facilityNameInput.getText().toString();
            String facilityAddressStr = facilityAddressInput.getText().toString();
            if (facilityName.isEmpty() || facilityAddressStr.isEmpty()) {
                throw new RuntimeException("Facility name and address cannot be empty");
            }
            // convert address to LatLng
            LatLng facilityAddress = getAddress(facilityAddressStr);
            if (facilityAddress == null) {
                throw new RuntimeException("Invalid address");
            }
            Facility facility = new Facility(facilityName, facilityAddress);

        });
    }
    public LatLng getAddress(String address) {
        // converts string address given by user to LatLng
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
