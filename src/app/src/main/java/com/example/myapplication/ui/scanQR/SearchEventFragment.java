package com.example.myapplication.ui.scanQR;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.Event;
import com.example.myapplication.OnSingleEventFetchListener;
import com.example.myapplication.R;

/**
 * This fragment is used to search for an event associated with a qr code.
 * If no event is found, it will inform the user.
 */
public class SearchEventFragment extends Fragment implements OnSingleEventFetchListener {
    View view;
    private ScanQRViewModel scanQRViewModel;
    DatabaseManager databaseManager;
    Event eventToView;
    TextView notifyMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search_event, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize variables and find views
        scanQRViewModel = new ViewModelProvider(requireActivity()).get(ScanQRViewModel.class);
        notifyMessage = view.findViewById(R.id.notify_message);
        eventToView = null;
        databaseManager = new DatabaseManager();
        databaseManager.getSingleEvent(scanQRViewModel.getEventPath(), this);

    }

    @Override
    public void onSingleEventFetch(Event event) {
        this.eventToView = event;
        if (eventToView != null) {
            scanQRViewModel.setEventToView(eventToView);
            try {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ViewEventDetailsFragment viewEventDetailsFragment = new ViewEventDetailsFragment();
                fragmentTransaction.replace(R.id.fragment_container, viewEventDetailsFragment);
                fragmentTransaction.commit();
            } catch (Exception e) {
                Log.e("SwitchFragment", "Error switching fragment:");
            }
        } else {
            requireActivity().runOnUiThread(() -> {
                notifyMessage.setText("No event found");
            });
        }


    }
}