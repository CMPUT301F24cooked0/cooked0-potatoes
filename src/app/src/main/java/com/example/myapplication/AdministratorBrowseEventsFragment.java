package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AdministratorBrowseEventsFragment extends Fragment {
    private ListView eventListView;
    private List<Event> eventList;
    private EventArrayAdapter eventArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.administrator_browse_events,container,false);
        eventListView=view.findViewById(R.id.administrator_event_list);
        eventList=new ArrayList<>();
        eventArrayAdapter=new EventArrayAdapter(requireContext(),eventList);
        eventListView.setAdapter(eventArrayAdapter);

        eventListView.setOnItemClickListener((parent,view1,position,id)->{
            Event clickedEvent=eventList.get(position);
            Intent intent=new Intent(getContext(),AdministratorEventDetails.class);
            startActivity(intent);
        });
        return view;
    }
}
