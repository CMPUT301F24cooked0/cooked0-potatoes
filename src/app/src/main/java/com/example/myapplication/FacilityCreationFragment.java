package com.example.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class FacilityCreationFragment extends AppCompatActivity {
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button createFacilityButton;
    User facilityOwner;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_creation);
        facilityNameInput = findViewById(R.id.addFacilityName);
        facilityAddressInput = findViewById(R.id.addFacilityAddress);
        createFacilityButton = findViewById(R.id.createFacilityButton);
        databaseManager = new DatabaseManager();
        facilityOwner = (User) getIntent().getSerializableExtra("user");
        createFacilityButton.setOnClickListener(view -> {
            // get facility name and address from input fields
            String facilityName = facilityNameInput.getText().toString();
            String facilityAddressStr = facilityAddressInput.getText().toString();
            if (facilityName.isEmpty() || facilityAddressStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // convert address to LatLng
            LatLng facilityAddress = getAddress(facilityAddressStr);
            if (facilityAddress == null) {
                Toast.makeText(this, "Invalid address", Toast.LENGTH_SHORT).show();
                return;
            }
            Facility facility = new Facility(facilityName, facilityAddress);
            facilityOwner.setFacility(facility);
            // add facility to database and set facility document reference
            facility.setFacilityReference(databaseManager.createFacility(facilityOwner, facility));


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
            e.printStackTrace();
            Toast.makeText(this, "Error getting address", Toast.LENGTH_SHORT).show();
            return null;
        }
        return null;
    }
}
