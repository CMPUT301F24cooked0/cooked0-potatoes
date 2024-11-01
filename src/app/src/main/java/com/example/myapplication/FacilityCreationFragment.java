package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FacilityCreationFragment extends AppCompatActivity {
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button createFacilityButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_creation);
        facilityNameInput = findViewById(R.id.addFacilityName);
        facilityAddressInput = findViewById(R.id.addFacilityName2);
        createFacilityButton = findViewById(R.id.createFacilityButton);
        createFacilityButton.setOnClickListener(view -> {
            String facilityName = facilityNameInput.getText().toString();
            String facilityAddressStr = facilityAddressInput.getText().toString();
        });
    }
}
