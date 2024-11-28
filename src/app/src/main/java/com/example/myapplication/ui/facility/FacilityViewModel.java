package com.example.myapplication.ui.facility;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.User;

public class FacilityViewModel extends ViewModel {

    User organizer;

    public FacilityViewModel() {
        organizer = null;

    }
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }
    public User getOrganizer() {
        return organizer;
    }

}