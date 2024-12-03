package com.example.myapplication.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button manageFacilitiesBtn=findViewById(R.id.manage_facilities_button);
        Button manageEventsBtn=findViewById(R.id.manage_events_button);
        Button manageUsersBtn=findViewById(R.id.manage_users_button);

        manageFacilitiesBtn.setOnClickListener(view -> {
            Intent intent=new Intent(this, AdminBrowseFacilitiesFragment.class);
            startActivity(intent);
        });

        manageEventsBtn.setOnClickListener(view -> {
            Intent intent=new Intent(this, AdminBrowseEventsFragment.class);
            startActivity(intent);
        });

        manageUsersBtn.setOnClickListener(view -> {
            Intent intent=new Intent(this, AdminBrowseUsersFragment.class);
            startActivity(intent);
        });
    }
}
