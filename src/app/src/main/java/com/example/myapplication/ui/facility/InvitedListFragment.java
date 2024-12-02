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
import com.example.myapplication.User;

import java.util.ArrayList;

public class InvitedListFragment extends Fragment {

    private RecyclerView recyclerView;
    private InvitedListAdapter adapter;
    private ArrayList<User> invitedParticipants;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invited_list, container, false);

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve the invited participants list passed from WaitingListFragment
        if (getArguments() != null) {
            invitedParticipants = getArguments().getParcelableArrayList("invitedParticipants");
        }

        // Set up Adapter for RecyclerView
        if (invitedParticipants != null) {
            adapter = new InvitedListAdapter(invitedParticipants);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}
