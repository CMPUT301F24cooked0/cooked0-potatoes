package com.example.myapplication.ui.facility;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.databinding.FacilityScreenFragmentBinding;

public class FacilityFragment extends Fragment {

    private FacilityScreenFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        User user;
        try {
            FacilityViewModel facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
            if (facilityViewModel.getOrganizer() == null) {
                user = new User("test", "test", "test@gmail.com");
                facilityViewModel.setOrganizer(user);
            } else {
                user = facilityViewModel.getOrganizer();
            }
            Toast.makeText(this.requireContext(), "User created", Toast.LENGTH_SHORT).show();
            if (user.getFacility() == null) {
                Toast.makeText(this.requireContext(), "Please create a facility", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new FacilityCreationFragment())
                        .commit();
            } else {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new FacilityEditFragment())
                        .commit();
            }

        } catch (Exception e) {
            Toast.makeText(this.requireContext(), "Unable to create user", Toast.LENGTH_SHORT).show();;
        }


        binding = FacilityScreenFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textFacility;
//        facilityViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}