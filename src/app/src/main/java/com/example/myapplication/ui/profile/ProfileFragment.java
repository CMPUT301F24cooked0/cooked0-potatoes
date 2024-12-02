package com.example.myapplication.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.AdminHomePage;
import com.example.myapplication.databinding.ProfileScreenFragmentBinding;

public class ProfileFragment extends Fragment {

    private ProfileScreenFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ProfileScreenFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.adminButton.setVisibility(View.GONE);
        binding.adminButtonFrame.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProfileViewModel profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.profileName.setText(user.getName());
                binding.profileEmail.setText(user.getEmail());
                binding.profilePhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber().toString() : "N/A");
                binding.profilePicture.setImageBitmap(user.getProfilePicture());
                if (user.isAdmin()) {
                    binding.adminButton.setVisibility(View.VISIBLE);
                    binding.adminButtonFrame.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.editButton.setOnClickListener(editButtonView -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });


        binding.adminButton.setOnClickListener(editButtonView -> {
            Intent intent = new Intent(getActivity(), AdminHomePage.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}