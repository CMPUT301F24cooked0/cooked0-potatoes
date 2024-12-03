package com.example.myapplication.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.AdminHomePageBinding;
import com.example.myapplication.databinding.ProfileScreenFragmentBinding;
import com.example.myapplication.ui.admin.events.AdminBrowseEventsFragment;
import com.example.myapplication.ui.admin.facilities.AdminBrowseFacilitiesFragment;
import com.example.myapplication.ui.admin.users.AdminBrowseUsersFragment;

public class AdminHomeFragment extends Fragment {

    private AdminHomePageBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = AdminHomePageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button manageEventsBtn = binding.manageEventsButton;
        Button manageFacilitiesBtn = binding.manageFacilitiesButton;
        Button manageUsersBtn = binding.manageUsersButton;

        manageEventsBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminHomeFragment.this)
                    .navigate(R.id.action_adminHomeFragment_to_adminBrowseEventsFragment);
        });

        manageFacilitiesBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminHomeFragment.this)
                    .navigate(R.id.action_adminHomeFragment_to_adminBrowseFacilitiesFragment);
        });

        manageUsersBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminHomeFragment.this)
                    .navigate(R.id.action_adminHomeFragment_to_adminBrowseUsersFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
