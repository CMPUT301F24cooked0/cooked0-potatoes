package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SignUpFragment extends Fragment {

    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profileImageView;
    private Button signUpButton;
    private Bitmap selectedImageBitmap;
    private ActivityResultLauncher<Intent> imagePickerLauncher;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        nameEditText = view.findViewById(R.id.name);
        emailEditText = view.findViewById(R.id.email);
        phoneEditText = view.findViewById(R.id.phone_number);
        profileImageView = view.findViewById(R.id.profile_image_view);
        signUpButton = view.findViewById(R.id.signup_button);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            selectedImageBitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(imageUri));
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

        // Set up the "Sign Up" button click listener
        signUpButton.setOnClickListener(v -> handleSignUp(v));
    }

    private void handleSignUp(View view) {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user details in SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Phone", phone);
        editor.apply();

        // Set onboarding as complete in SharedPreferences
        SharedPreferences onboardingPreferences = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        onboardingPreferences.edit().putBoolean("onboarding_complete", true).apply();

        // Navigate to the main activity
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_signUpFragment_to_mainActivity);
    }
}
