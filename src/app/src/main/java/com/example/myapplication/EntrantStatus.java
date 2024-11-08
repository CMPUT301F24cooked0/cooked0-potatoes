package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;

/*
This class is responsible for getting and setting an entrants status. It also allows organizers
to send notifications to entrants.
 */
public class EntrantStatus {
    private final User entrant;
    private final LatLng joinedFrom;
    private Status status;

    /**
     * Simplest constructor for an EntrantStatus.
     * Sets the entrant's status to "none", which means that no draw has occurred yet
     * @param entrant
     * @param joinedFrom
     */
    public EntrantStatus(User entrant, LatLng joinedFrom) {
        this.entrant = entrant;
        this.joinedFrom = joinedFrom;
        this.status = Status.none; // starting status is none (no draw has occurred yet)
        // TODO update database
    }

    /**
     * The same as the simplest constructor but allows setting a specific status
     * @param entrant
     * @param joinedFrom
     * @param status
     */
    public EntrantStatus(User entrant, LatLng joinedFrom, Status status) {
        this(entrant, joinedFrom);
        this.status = status; // this constructor allows setting a different starting status
    }

    /**
     * sets this entrant's status
     * @param status
     */
    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
            // TODO update database
        }
    }

    /**
     * send a notification to this entrant
     * @param notification
     */
    public void sendNotification(String notification) {
        NotificationSender.sendNotification(this.entrant, notification);
    }

    /**
     * get this entrant (user)
     * @return
     */
    public User getEntrant() {
        return this.entrant;
    }

    /**
     * get this entrant's status
     * @return
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * get the location from which the user joined the event
     * @return
     */
    public LatLng getJoinedFrom() {
        return this.joinedFrom;
    }
}
