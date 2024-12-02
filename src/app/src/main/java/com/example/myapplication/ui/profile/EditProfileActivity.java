package com.example.myapplication.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.MainActivity;
import com.example.myapplication.OnUserFetchListener;
import com.example.myapplication.ProfilePictureGenerator;
import com.example.myapplication.R;
import com.example.myapplication.User;

/**
 * This class is used currently as an Activity for the User to edit details of their profile.
 * @author Ishaan Chandel, Daniyal Abbas
 * @version 1.0
 */
public class EditProfileActivity extends AppCompatActivity implements OnUserFetchListener {
    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profileImageView;
    private Button saveButton;
    private Bitmap selectedImageBitmap;
    private DatabaseManager dbManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize views
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone_number);
        profileImageView = findViewById(R.id.profile_image_view);
        saveButton = findViewById(R.id.save_button);

        // fetching user from database
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        dbManager = new DatabaseManager();
        dbManager.getUser(deviceID, this);

        // Launch image picker
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

        saveButton.setOnClickListener(view -> saveUserDetails());
    }

    /**
     * Saves the newly entered user details database
     * @author Ishaan Chandel, Daniyal Abbas
     */
    private void saveUserDetails() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        Long phone = null;

        if (!phoneEditText.getText().toString().isEmpty()) {  // if there is input in the phoneNumber field

            if (phoneEditText.getText().toString().length() < 15 && phoneEditText.getText().toString().length() > 8) {  // if the phone number is 10 digits

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

        // Save updated details to databases
        try {
            user.setName(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            user.setEmail(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            user.setPhoneNumber(phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        user.setProfilePicture(selectedImageBitmap);

        dbManager.updateUser(user);

        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

        // Redirect back to MainActivity
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close EditProfileActivity
    }

    @Override
    public void onUserFetch(User user) {
        if (user == null) {
            new DatabaseManager().createUser(this.user);
        }
        else {
            this.user = user;

            runOnUiThread(() -> {
                nameEditText.setText(user.getName() != null ? user.getName() : "");
                emailEditText.setText(user.getEmail() != null ? user.getEmail() : "");
                phoneEditText.setText(user.getPhoneNumber() != null ? user.getPhoneNumber().toString() : "");

                Bitmap profileImage = user.getProfilePicture();
                if (profileImage != null) {
                    profileImageView.setImageBitmap(profileImage);
                }
            });
        }
    }
}
