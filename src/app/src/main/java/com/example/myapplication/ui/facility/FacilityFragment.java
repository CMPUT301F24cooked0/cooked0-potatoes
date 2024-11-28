package com.example.myapplication.ui.facility;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
        user = (User) getArguments().getSerializable("user");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}