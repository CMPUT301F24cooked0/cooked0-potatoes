package com.example.myapplication.ui.admin.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.Event;
import com.example.myapplication.EventArrayAdapter;
import com.example.myapplication.databinding.AdminBrowseEventsBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminBrowseEventsFragment extends Fragment {

    private AdminBrowseEventsBinding binding;
    private List<Event> eventList;
    private EventArrayAdapter eventArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // Inflate layout using View Binding
        binding = AdminBrowseEventsBinding.inflate(inflater, container, false);


        eventList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(requireContext(),eventList);
        binding.adminEventList.setAdapter(eventArrayAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseManager dbManager = new DatabaseManager();
        dbManager.getAllEvents(events -> {
            if(events!=null && !events.isEmpty()){
                requireActivity().runOnUiThread(()->{
                    eventList.clear();
                    eventList.addAll(events);
                    eventArrayAdapter.notifyDataSetChanged();
                    Log.d("Fetch Events","Success");
                });
            }
            else{
                requireActivity().runOnUiThread(()->{
                    Log.w("Fetch Events","Failed");
                    Toast.makeText(requireContext(),"No Events Found",Toast.LENGTH_SHORT).show();
                });
            }
        });

        binding.adminEventList.setOnItemClickListener((parent, v, position, id) -> {
            Event selectedEvent = eventList.get(position);
            String eventRefPath = selectedEvent.getEventReference().getPath();
            Intent intent = new Intent(getContext(), AdminEventDetails.class);
            intent.putExtra("eventRef", eventRefPath);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
