package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * This class represents the launch screen of the app for brand new users
 * @author Ishaan Chandel, Daniyal Abbas
 */
public class WelcomeActivity extends AppCompatActivity implements OnUserFetchListener {

    private Button btn;
    private DatabaseManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn = findViewById(R.id.welcome_button);
        btn.setVisibility(View.GONE);

        dbManager = new DatabaseManager();

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        dbManager.getUser(deviceId, this);

    }

    @Override
    public void onUserFetch(User user) {
        if (user == null) {
            // Device ID does not exist in Firestore
            runOnUiThread(() -> {
                btn.setVisibility(View.VISIBLE); // Show the button
                btn.setOnClickListener(v -> {
                    Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                });
            });
        } else {
            // Device ID exists in Firestore
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}