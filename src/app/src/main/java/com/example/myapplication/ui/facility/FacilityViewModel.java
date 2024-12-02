package com.example.myapplication.ui.facility;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.Event;
import com.example.myapplication.User;


public class FacilityViewModel extends ViewModel {

    private User organizer;
    private Event eventToManage;

    public FacilityViewModel() {
        organizer = null;
        eventToManage = null;

    }
    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public void setEventToManage(Event event) {
        eventToManage = event;
    }
    public Event getEventToManage() {
        return eventToManage;
    }



}