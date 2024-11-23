package com.example.myapplication;

import android.annotation.SuppressLint;
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
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
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
        setContentView(binding.getRoot());

        // Set up the NavController for bottom navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navbar_inbox, R.id.navbar_facility, R.id.navbar_scanQR, R.id.navbar_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);


        /*replaceFragment(new NotificationsScreenFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navbar_inbox) {
                replaceFragment(new NotificationsScreenFragment());
                return true;
            }
            else if (item.getItemId() == R.id.navbar_facility) {
                replaceFragment(new FacilityScreenFragment());
                return true;
            }
            else if (item.getItemId() == R.id.navbar_scanQR) {
                replaceFragment(new ScanQRScreenFragment());
                return true;
            }
            else if (item.getItemId() == R.id.navbar_profile) {
                replaceFragment(new ProfileScreenFragment());
                return true;
            }
            else {
                return false;
            }

            });


         */

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
