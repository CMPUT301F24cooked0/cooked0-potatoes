package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FacilityCreationFragment extends Fragment {

    View view;
    EditText facilityNameInput;
    EditText facilityAddressInput;
    Button createFacilityButton;
    User facilityOwner;
    DatabaseManager databaseManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.facility_creation, container, false);
        facilityNameInput = view.findViewById(R.id.addFacilityName);
        facilityAddressInput = view.findViewById(R.id.addFacilityAddress);
        createFacilityButton = view.findViewById(R.id.createFacilityButton);
        databaseManager = new DatabaseManager();


        return view;
    }
}
