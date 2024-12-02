package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.facility.FacilityViewModel;
import com.example.myapplication.ui.profile.ProfileViewModel;
import com.example.myapplication.ui.scanQR.ScanQRViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements OnUserFetchListener {

    private ActivityMainBinding binding;

    private User user;
    private DatabaseManager dbManager;



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate Layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inbox, R.id.navigation_facility, R.id.navigation_scanQR, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        // fetching user from database
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        dbManager = new DatabaseManager();
        dbManager.getUser(deviceID, this);

    }

    @Override
    public void onUserFetch(User user) {
        if (user == null) {
            new DatabaseManager().createUser(this.user);
        } else {
            this.user = user;

            // Pass the user to ViewModels
            ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            profileViewModel.setUser(user);

            FacilityViewModel facilityViewModel = new ViewModelProvider(this).get(FacilityViewModel.class);
            facilityViewModel.setOrganizer(user);

            ScanQRViewModel scanQRViewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);
            scanQRViewModel.setUser(user);
        }
    }
}