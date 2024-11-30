package com.example.myapplication.ui.scanQR;


import androidx.lifecycle.ViewModel;

import com.example.myapplication.Event;
import com.example.myapplication.User;

public class ScanQRViewModel extends ViewModel {

    private User user;
    private Event eventToView;
    private String eventPath;

    public ScanQRViewModel() {
        user = null;
        eventToView = null;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return this.user;
    }
    public void setEventToView(Event event) {
        this.eventToView = event;
    }
    public Event getEventToView() {
        return this.eventToView;
    }
    public void setEventPath(String path) {
        this.eventPath = path;
    }
    public String getEventPath() {
        return this.eventPath;
    }

}