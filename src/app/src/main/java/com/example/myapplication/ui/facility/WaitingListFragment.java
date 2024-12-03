package com.example.myapplication.ui.facility;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.EntrantPool;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.ui.facility.WaitingListAdapter;

import java.util.ArrayList;

public class WaitingListFragment extends Fragment {

    private RecyclerView recyclerView;
    private WaitingListAdapter adapter;
    private EntrantPool entrantPool;
    private ArrayList<User> invitedParticipants = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waiting_list, container, false);

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        entrantPool = EntrantPool.getInstance();
        if (entrantPool == null) {
            Log.e("WaitingListFragment", "EntrantPool is null");
            return view;
        }

        ArrayList<User> waitingListUsers = entrantPool.getWaitingList();
        if (waitingListUsers == null) {
            Log.e("WaitingListFragment", "Waiting list is null");
            return view;
        }

        updateRecyclerView(waitingListUsers);

        // Set up Choose Participants button
        Button chooseParticipantsButton = view.findViewById(R.id.chooseParticipantsButton);
        chooseParticipantsButton.setOnClickListener(v -> chooseParticipants(2)); // Choose 2 participants for testing

        // Set up View Invited List button
        Button viewInvitedListButton = view.findViewById(R.id.invite_button);
        viewInvitedListButton.setOnClickListener(v -> navigateToInvitedList());

        return view;
    }

    private void updateRecyclerView(ArrayList<User> waitingListUsers) {
        if (waitingListUsers == null || waitingListUsers.isEmpty()) {
            Log.e("WaitingListFragment", "Waiting list is empty or null");
            return;
        }

        ArrayList<String> waitingListNames = new ArrayList<>();
        for (User user : waitingListUsers) {
            waitingListNames.add(user.getName());
        }

        // Initialize and set the adapter
        adapter = new WaitingListAdapter(waitingListNames);
        recyclerView.setAdapter(adapter);
    }


    // Method to choose participants (mock functionality for now)
    private void chooseParticipants(int howMany) {
        // Simulate participant drawing using the EntrantPool's method
        ArrayList<User> drawnParticipants = entrantPool.drawEntrants(howMany);
        invitedParticipants.addAll(drawnParticipants);
    }

    // Method to navigate to InvitedListFragment with invited participants
    private void navigateToInvitedList() {
        // Pass the invited participants list to the InvitedListFragment using arguments
        Bundle bundle = new Bundle();

        // Assuming you're passing some data here, update this as needed
        //bundle.putParcelableArrayList("invitedParticipants", invitedParticipants);

        NavController navController = NavHostFragment.findNavController(WaitingListFragment.this);
        navController.navigate(R.id.navigation_invited_list, bundle);
    }
}
