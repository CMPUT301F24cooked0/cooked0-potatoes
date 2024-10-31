package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;

public class EntrantStatus {
    private final User entrant;
    private final LatLng joinedFrom;
    private Status status;

    public EntrantStatus(User entrant, LatLng joinedFrom) {
        this.entrant = entrant;
        this.joinedFrom = joinedFrom;
        this.status = Status.none; // starting status is none (no draw has occurred yet)
        // TODO update database
    }

    public EntrantStatus(User entrant, LatLng joinedFrom, Status status) {
        this(entrant, joinedFrom);
        this.status = status; // this constructor allows setting a different starting status
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
            // TODO update database
        }
    }

    public void sendNotification(String notification) {
        NotificationSender.sendNotification(this.entrant, notification);
    }

    public User getEntrant() {
        return this.entrant;
    }

    public Status getStatus() {
        return this.status;
    }

    public LatLng getJoinedFrom() {
        return this.joinedFrom;
    }
}
