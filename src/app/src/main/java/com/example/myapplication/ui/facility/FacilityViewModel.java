package com.example.myapplication.ui.facility;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.User;

public class FacilityViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private User organizer;

    public FacilityViewModel() {
        organizer = null;
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    public User getOrganizer() {
        return organizer;
    }
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public LiveData<String> getText() {
        return mText;
    }
}