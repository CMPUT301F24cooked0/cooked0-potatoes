package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminHomePage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page);

        Button manageFacilitiesBtn=findViewById(R.id.manage_facilities_button);
        Button manageEventsBtn=findViewById(R.id.manage_events_button);
        Button manageUsersBtn=findViewById(R.id.manage_users_button);

        manageFacilitiesBtn.setOnClickListener(view -> {
            Intent intent=new Intent(this,AdministratorBrowseFacilities.class);
            startActivity(intent);
        });

        manageEventsBtn.setOnClickListener(view -> {
            Intent intent=new Intent(this,AdministratorBrowseEventsFragment.class);
            startActivity(intent);
        });

        manageUsersBtn.setOnClickListener(view -> {
            Intent intent=new Intent(this, AdministratorBrowseUsersFragment.class);
            startActivity(intent);
        });
    }
}
