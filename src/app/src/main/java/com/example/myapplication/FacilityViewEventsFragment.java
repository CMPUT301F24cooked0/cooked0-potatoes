package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class FacilityViewEventsFragment extends AppCompatActivity {
    TextView facilityName;
    FrameLayout facilityEditFrame;
    ListView eventList;
    EventArrayAdapter eventAdapter;
    Facility facility;
    FloatingActionButton addEventButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_view_events);
        facility = (Facility) getIntent().getSerializableExtra("facility");
        facilityName = findViewById(R.id.facilityNamePlaceholder);
        facilityEditFrame = findViewById(R.id.welcomeFacilityFrame);
        eventList = findViewById(R.id.eventList);
        addEventButton = findViewById(R.id.addBtn);
        eventAdapter = new EventArrayAdapter(this, facility.getEvents());
        eventList.setAdapter(eventAdapter);
        facilityName.setText(facility.getName());
        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateEventFragment.class);
            intent.putExtra("facility", facility);
            startActivity(intent);
        });
        // TODO Link with edit facility page
//        facilityEditFrame.setOnClickListener(v -> {
//            Intent intent = new Intent(this, EditFacilityFragment.class);
//            intent.putExtra("facility", facility);
//            startActivity(intent);
//        });
        // TODO Link with edit event page
        eventList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, EditEventFragment.class);
            intent.putExtra("event", facility.getEvents().get(position));
            startActivity(intent);
        });

        }

    }
}
