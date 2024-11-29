package com.example.myapplication.ui.facility;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.databinding.FacilityScreenFragmentBinding;

public class FacilityFragment extends Fragment {

    private FacilityScreenFragmentBinding binding;
    FacilityViewModel facilityViewModel;
    User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FacilityScreenFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onViewCreated (@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        user = facilityViewModel.getOrganizer(); // get user from view model
//        user = (User) getArguments().getSerializable("user");
        if (user != null) {
            facilityViewModel.setOrganizer(user);

            // change fragments depending on whether the user has a facility or not
            if (user.getFacility() != null) {
                // TODO show facility view events page once merged in
                Toast.makeText(this.requireContext(), "User has facility", Toast.LENGTH_SHORT).show(); // temporary until facility view events page is merged
            } else {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new FacilityCreationFragment())
                        .commit();
            }

        } else {
            Toast.makeText(this.requireContext(), "User not found", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}