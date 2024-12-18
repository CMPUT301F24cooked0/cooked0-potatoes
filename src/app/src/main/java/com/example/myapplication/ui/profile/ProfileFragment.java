package com.example.myapplication.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ui.admin.AdminActivity;
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
                binding.profilePhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber().toString() : "");
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
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}