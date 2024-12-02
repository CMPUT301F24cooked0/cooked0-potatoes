package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(Context context, List<Event> events){
        super(context,0,events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Event event=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.event_summary,parent,false);
        }
        ImageView eventPoster=convertView.findViewById(R.id.event_poster);
        TextView eventName=convertView.findViewById(R.id.event_name);
        //TextView eventDescription=convertView.findViewById(R.id.event_description);

        if(event.getEventPoster()!=null){
            eventPoster.setImageBitmap(event.getEventPoster());
        }
        eventName.setText(event.getName());
        //eventDescription.setText(event.getDescription()); TODO add a description in the event class or show date and capacity
        return convertView;
    }


}
