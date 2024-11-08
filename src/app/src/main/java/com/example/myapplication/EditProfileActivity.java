package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
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

/**
 * This class is used currently as an Activity for the User to edit details of their profile.
 * @author Daniyal Abbass, Ishaan Chandel
 * @version 1.0
 */
public class EditProfileActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profileImageView;
    private Button saveButton;
    private Bitmap selectedImageBitmap;

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
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone_number);
        profileImageView = findViewById(R.id.profile_image_view);
        saveButton = findViewById(R.id.save_button);

        // Load existing user details
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        nameEditText.setText(sharedPreferences.getString("Name", ""));
        emailEditText.setText(sharedPreferences.getString("Email", ""));
        phoneEditText.setText(sharedPreferences.getString("Phone", ""));

        // Load existing profile image
        String encodedImage = sharedPreferences.getString("ProfileImage", null);
        if (encodedImage != null) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageView.setImageBitmap(decodedByte);
        }

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
     * Saves the newly enterred user details to SharedPreferences.
     * @author Daniyal Abbas
     */
    private void saveUserDetails() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageBitmap == null) {
            selectedImageBitmap = generateProfileImage(name);
        }

        // Convert bitmap to Base64 if a new image is selected
        String encodedImage = null;
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }

        // Save updated details to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Phone", phone);
        if (encodedImage != null) {
            editor.putString("ProfileImage", encodedImage);
        }
        editor.apply();

        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

        // Redirect back to MainActivity
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close EditProfileActivity
    }

    /**
     * Generates a profile image based on the first letter of the user's name.
     * @author Daniyal Abbas
     * @param name - String storing name of the user to perform profile picture generation with
     * @return
     */
    private Bitmap generateProfileImage(String name) {
        String firstNameInitial = name.length() > 0 ? name.substring(0, 1).toUpperCase() : "A";

        int size = 200;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw a circle
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);  // Set background color
        paint.setAntiAlias(true);
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);

        Rect textBounds = new Rect();
        paint.getTextBounds(firstNameInitial, 0, firstNameInitial.length(), textBounds);
        int x = size / 2;
        int y = size / 2 + (textBounds.height() / 2);

        canvas.drawText(firstNameInitial, x, y, paint);

        return bitmap;
    }
}
