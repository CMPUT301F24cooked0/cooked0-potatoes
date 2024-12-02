package com.example.myapplication.ui.facility;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;

public class ViewParticipantsLists extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_lists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button waitingListButton = view.findViewById(R.id.waiting_list_button);
        waitingListButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(ViewParticipantsLists.this);
            navController.navigate(R.id.navigation_waiting_list);
        });

        Button acceptedListButton = view.findViewById(R.id.accepted_list_button);
        acceptedListButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(ViewParticipantsLists.this);
            navController.navigate(R.id.navigation_accepted_list);
        });

        Button declinedListButton = view.findViewById(R.id.declined_list_button);
        declinedListButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(ViewParticipantsLists.this);
            navController.navigate(R.id.navigation_declined_list);
        });

    }
}
