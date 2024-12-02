package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.ByteArrayOutputStream;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * This class is used currently as an Activity for the User to sign up by adding details of their profile.
 * @author Daniyal Abbass, Ishaan Chandel
 * @version 1.0
 */
public class SignUpActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profileImageView;
    private Button signupButton, selectImageButton;
    private Bitmap selectedImageBitmap;
    private DatabaseManager dbManager;
    private User user;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbManager = new DatabaseManager();

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone_number);
        profileImageView = findViewById(R.id.profile_image_view);
        signupButton = findViewById(R.id.signup_button);

        ActivityResultLauncher<Intent> imagePickerLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            selectedImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            profileImageView.setImageBitmap(selectedImageBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        signupButton.setOnClickListener(view -> saveUserDetails());
    }

    /**
     * Saves the user details to SharedPreferences.
     * @author Ishaan Chandel, Daniyal Abbas
     */
    private void saveUserDetails() {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        Long phone = null;

        if (!phoneEditText.getText().toString().isEmpty()) {  // if there is input in the phoneNumber field

            if (phoneEditText.getText().toString().length() == 10) {  // if the phone number is 10 digits

                try {
                    phone = Long.parseLong(phoneEditText.getText().toString());
                } catch (NumberFormatException e) {
                    // Handle invalid input (e.g., non-numeric characters)
                    Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    return; // Stop further processing
                }

            } else {

                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();

                return; // Stop further processing
            }

        }

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageBitmap == null) {
            selectedImageBitmap = ProfilePictureGenerator.generateProfileImage(name);
        }

        // Converting bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        // Creating the User object
        try {
            user = new User(deviceId, name, email, phone, selectedImageBitmap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Create User in database
        dbManager.createUser(user);

        Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

        // Switch to MainActivity
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
