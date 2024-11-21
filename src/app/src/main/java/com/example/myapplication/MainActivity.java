package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.facility.FacilityFragment;
import com.example.myapplication.ui.notifications.NotificationsFragment;
import com.example.myapplication.ui.profile.ProfileFragment;
import com.example.myapplication.ui.scanQR.ScanQRFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private TextView profileTextView;
    private ImageView profileImageView;
    private Button signOut;
    private ImageButton editUserInfo;



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        replaceFragment(new NotificationsFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navbar_inbox:
                    replaceFragment(new NotificationsFragment());
                    break;
                case R.id.navbar_facility:
                    replaceFragment(new FacilityFragment());
                    break;
                case R.id.navbar_scanQR:
                    replaceFragment(new ScanQRFragment());
                    break;
                case R.id.navbar_profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
            });


        SharedPreferences preferences = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean isOnboardingComplete = preferences.getBoolean("onboarding_complete", false);

        if (!isOnboardingComplete) {
            // Launch onboarding activity if onboarding is not complete
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
        }
        else {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Set up the NavController for bottom navigation
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
            NavigationUI.setupWithNavController(bottomNav, navController);


            profileTextView = findViewById(R.id.profile_text);
            profileImageView = findViewById(R.id.my_profile);
            signOut=findViewById(R.id.signout_button);
            editUserInfo=findViewById(R.id.edit_button);
            SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
            String name = sharedPreferences.getString("Name", "N/A");
            String email = sharedPreferences.getString("Email", "N/A");
            String phone = sharedPreferences.getString("Phone", "N/A");
            String encodedImage = sharedPreferences.getString("ProfileImage", null);


            String profileDetails = "Name: " + name + "\nEmail: " + email + "\nPhone: " + phone;
            profileTextView.setText(profileDetails);

            if (encodedImage != null) {
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profileImageView.setImageBitmap(decodedByte);
            }

            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOutUser();
                }
            });

            editUserInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                    startActivity(intent);

                }
            });


        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);

    }

    private void signOutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.signUpFragment);
    }
}
