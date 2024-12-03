package com.example.myapplication.ui.facility;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class InvitedParticipantsFragment extends Fragment {

    private ArrayList<String> invitedNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invited_list, container, false);

        // Retrieve the invited names list from the arguments
        if (getArguments() != null) {
            invitedNames = getArguments().getStringArrayList("invitedNames");
        }

        // Populate the RecyclerView with the invited names
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up your adapter with the invited names
        InvitedParticipantsAdapter adapter = new InvitedParticipantsAdapter(invitedNames); // Pass the names list to your adapter
        recyclerView.setAdapter(adapter);

        return view;
    }
}
