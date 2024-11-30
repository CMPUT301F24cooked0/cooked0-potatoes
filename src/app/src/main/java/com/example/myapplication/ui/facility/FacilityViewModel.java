package com.example.myapplication.ui.facility;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Event;
import com.example.myapplication.Facility;
import com.example.myapplication.User;

import java.util.ArrayList;

public class FacilityViewModel extends ViewModel {

    private User organizer;
    MutableLiveData<ArrayList<Event>> events;

    public FacilityViewModel() {
        organizer = null;
        events = null;

    }
    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public void setEvents() {
        events = new MutableLiveData<>();
        Facility facility = this.organizer.getFacility();
        events.setValue(facility.getEvents());

    }

    public MutableLiveData<ArrayList<Event>> getEvents() {
        return events;

    }


}