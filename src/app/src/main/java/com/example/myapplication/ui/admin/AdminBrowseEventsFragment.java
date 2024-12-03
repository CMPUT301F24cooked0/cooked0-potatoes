package com.example.myapplication.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.Event;
import com.example.myapplication.EventArrayAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AdminBrowseEventsFragment extends Fragment {
    private ListView eventListView;
    private List<Event> eventList;
    private EventArrayAdapter eventArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.admin_browse_events,container,false);
        eventListView=view.findViewById(R.id.administrator_event_list);
        eventList=new ArrayList<>();
        eventArrayAdapter=new EventArrayAdapter(requireContext(),eventList);
        eventListView.setAdapter(eventArrayAdapter);
        DatabaseManager databaseManager=new DatabaseManager();
        databaseManager.getAllEvents(events -> {
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

        eventListView.setOnItemClickListener((parent,view1,position,id)->{
            Event selectedEvent=eventList.get(position);
            String eventRefPath=selectedEvent.getEventReference().getPath();
            Intent intent=new Intent(getContext(), AdminEventDetails.class);
            intent.putExtra("eventRef",eventRefPath);
            startActivity(intent);
        });
        return view;
    }
}
