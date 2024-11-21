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

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements OnUserFetchListener {
    private TextView profileTextView;
    private ImageView profileImageView;
    private Button signOut;
    private ImageButton editUserInfo;
    private User user;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profileTextView = findViewById(R.id.prfile_text);
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
        DatabaseManager databaseManager = new DatabaseManager();
        Long phoneNumber = Long.parseLong(phone);
        try {
            user = new User("12345", name, email, phoneNumber, decodedByte);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        user.setFacility(new Facility("test name", new LatLng(42.69, 69.42)));
        try {
            user.getFacility().addEvent(new Event("event name", new Date(), null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            user.getFacility().getEvents().get(0).addEntrant(new User("54321", "test entrant", "entrant@ualberta.ca"), new LatLng(69.42, 42.69));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        databaseManager.createUser(user);

        databaseManager.getUser("12345", this);

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
