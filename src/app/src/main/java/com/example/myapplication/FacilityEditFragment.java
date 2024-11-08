package com.example.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class FacilityEditFragment extends AppCompatActivity {
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button editButton;
    Facility existingFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_edit);
        facilityNameInput = findViewById(R.id.editFacilityName);
        facilityAddressInput = findViewById(R.id.editFacilityAddress);
        editButton = findViewById(R.id.editFacilityButton);
        existingFacility = (Facility) getIntent().getSerializableExtra("facility");
        facilityNameInput.setText(existingFacility.getName()); // autofill existing facility name
        String address = latLngtoAddress(existingFacility.getLocation()); // convert LatLng to address
        facilityAddressInput.setText(address); // autofill existing facility address
        editButton.setOnClickListener(view -> {
            // get facility name and address from input fields
            String facilityName = facilityNameInput.getText().toString();
            String facilityAddressStr = facilityAddressInput.getText().toString();
            if (facilityName.isEmpty() || facilityAddressStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            LatLng facilityAddress = getAddress(facilityAddressStr);
            if (facilityAddress == null) {
                Toast.makeText(this, "Invalid address", Toast.LENGTH_SHORT).show();
                return;
            }
            existingFacility.setName(facilityName);
            existingFacility.setLocation(facilityAddress);




        });

    }
    public String latLngtoAddress(LatLng location) {
        Geocoder geocode = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocode.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public LatLng getAddress(String address) {
        Geocoder geocode1 = new Geocoder(this);
        try {
            List<Address> addresses = geocode1.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());

        }    } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
