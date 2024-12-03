package com.example.myapplication.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button manageFacilitiesBtn=findViewById(R.id.manage_facilities_button);
        Button manageEventsBtn=findViewById(R.id.manage_events_button);
        Button manageUsersBtn=findViewById(R.id.manage_users_button);

        manageFacilitiesBtn.setOnClickListener(view -> loadFragment(new AdministratorBrowseFacilitiesFragment()));

        manageEventsBtn.setOnClickListener(view -> loadFragment(new AdministratorBrowseEventsFragment()));

        manageUsersBtn.setOnClickListener(view -> loadFragment(new AdministratorBrowseUsersFragment()));

        /*
        manageUsersBtn.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            AdministratorBrowseUsersFragment fragment = new AdministratorBrowseUsersFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null); // Add to back stack to allow back navigation
            fragmentTransaction.commit();
        });

         */

    }

    /**
     * Method to load a fragment into the FrameLayout container.
     *
     * @param fragment the fragment to load.
     */
    private void loadFragment(Fragment fragment) {

        findViewById(R.id.admin_button_container).setVisibility(View.GONE);

        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            findViewById(R.id.admin_button_container).setVisibility(View.VISIBLE);
            findViewById(R.id.fragment_container).setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

}
