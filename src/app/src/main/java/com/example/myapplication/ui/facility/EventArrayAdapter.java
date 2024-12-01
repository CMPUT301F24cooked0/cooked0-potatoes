package com.example.myapplication.ui.facility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Event;
import com.example.myapplication.R;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(Context context, ArrayList<Event> events) {super(context, 0, events);}
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.events_array_content,
                    parent, false);
        } else {
            view = convertView;
        }
        Event event = getItem(position);
        TextView eventTitle = view.findViewById(R.id.eventNamePlaceholder);
        // TODO implement event description
        // TextView eventDesc = view.findViewById(R.id.eventDescPlaceholder);
        ImageView eventPoster = view.findViewById(R.id.eventPosterPlaceholder);
        eventTitle.setText(event.getName());
        eventPoster.setImageBitmap(event.getEventPoster());
        // eventDesc.setText(event.getDescription());
        return view;


    }

}
