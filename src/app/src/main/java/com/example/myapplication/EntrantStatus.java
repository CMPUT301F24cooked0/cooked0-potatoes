package com.example.myapplication;

import android.location.Location;

public class EntrantStatus {
    private final User entrant;
    private final Location joinedFrom;
    private Status status;

    public EntrantStatus(User entrant, Location joinedFrom) {
        this.entrant = entrant;
        this.joinedFrom = joinedFrom;
        this.status = Status.none; // starting status is none (no draw has occurred yet)
    }

    public EntrantStatus(User entrant, Location joinedFrom, Status status) {
        this(entrant, joinedFrom);
        this.status = status; // this constructor allows setting a different starting status
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
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

    public Location getJoinedFrom() {
        return this.joinedFrom;
    }
}
