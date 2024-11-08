package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavDestination;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Set up NavController for onboarding flow
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_onboarding);
        NavController navController = navHostFragment.getNavController();

        navController.addOnDestinationChangedListener((NavController controller, NavDestination destination, Bundle arguments) -> {
            if (destination.getId() == R.id.signUpFragment) {
                // User has reached the sign-up fragment and is completing onboarding
                SharedPreferences preferences = getSharedPreferences("onboarding", MODE_PRIVATE);
                preferences.edit().putBoolean("onboarding_complete", true).apply();

                // Launch main activity after completing onboarding
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the onboarding activity so the user can't navigate back
            }
        });
    }
}
