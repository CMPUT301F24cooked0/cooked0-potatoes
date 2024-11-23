package com.example.myapplication;

import java.util.ArrayList;

public interface OnEntrantStatusesFetchListener {
    void onEntrantStatusesFetch(Event event, ArrayList<EntrantStatus> entrantStatuses);
}
