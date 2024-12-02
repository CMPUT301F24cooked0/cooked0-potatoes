package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminFacilityDetails extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_facility_details);

        String facilityRefPath=getIntent().getStringExtra("facilityRef");
        if(facilityRefPath==null || facilityRefPath.isEmpty()){
            Toast.makeText(this,"Invalid Facility Reference",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


    }
}
