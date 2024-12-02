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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.facility.FacilityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements OnUserFetchListener {

    private ActivityMainBinding binding;

    private TextView profileTextView;
    private ImageView profileImageView;
    private Button signOut;
    private ImageButton editUserInfo;
    private User user;



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
        // test user (to use this you need to comment out the "setProfilePicture" line in User.java)
//        try {
//            user = new User("test", "test", "test@gmail.com");
//        } catch (Exception e) {
//            Toast.makeText(this, "Unable to create user", Toast.LENGTH_SHORT).show();
//        }
        // add user to facility view model
        FacilityViewModel facilityViewModel = new ViewModelProvider(this).get(FacilityViewModel.class);
        facilityViewModel.setOrganizer(user);


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

        Bitmap decodedByte = null;
        if (encodedImage != null) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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

    private void signOutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onUserFetch(User user) {
        if (user == null) {
            new DatabaseManager().createUser(this.user);
        }
        else {
            this.user = user;
        }
    }
}
