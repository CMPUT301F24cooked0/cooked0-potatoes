package com.example.myapplication;

import java.util.ArrayList;

public interface OnEventsFetchListener {
    void onEventsFetch(Facility facility, ArrayList<Event> events);
}
