package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;

/*
This class is responsible for getting and setting an entrants status. It also allows organizers
to send notifications to entrants.
 */
public class EntrantStatus {
    private final User entrant;
    private final LatLng joinedFrom;
    private Status status;
    private DocumentReference entrantStatusRef;

    /**
     * Simplest constructor for an EntrantStatus.
     * Sets the entrant's status to "none", which means that no draw has occurred yet
     * @param entrant
     * @param joinedFrom
     */
    public EntrantStatus(User entrant, LatLng joinedFrom) {
        if (entrant == null) {
            throw new RuntimeException("EntrantStatus entrant cannot be null");
        }
        this.entrant = entrant;
        this.joinedFrom = joinedFrom;
        this.setStatus(Status.none); // starting status is none (no draw has occurred yet)
    }

    /**
     * The same as the simplest constructor but allows setting a specific status
     * @param entrant
     * @param joinedFrom
     * @param status
     */
    public EntrantStatus(User entrant, LatLng joinedFrom, Status status) {
        this(entrant, joinedFrom);
        this.setStatus(status); // this constructor allows setting a different starting status
    }

    /**
     * only use this constructor in DatabaseManager to instantiate an EntrantStatus from the data in the database
     * @param entrant
     * @param joinedFrom
     * @param status
     * @param entrantStatusRef
     */
    public EntrantStatus(User entrant, LatLng joinedFrom, Status status, DocumentReference entrantStatusRef) {
        this.entrant = entrant;
        this.joinedFrom = joinedFrom;
        this.setStatus(status);
        this.entrantStatusRef = entrantStatusRef;
    }

    /**
     * sets this entrant's status
     * @param status
     */
    public void setStatus(Status status) {
        if (status == null) {
            return;
        }
        this.status = status;
    }

    /**
     * send a notification to this entrant
     * @param notification
     */
    public void sendNotification(String notification) throws Exception {
        NotificationManager.sendNotification(this.entrant, notification);
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

    public void setEntrantStatusReference(DocumentReference entrantStatusRef) {
        this.entrantStatusRef = entrantStatusRef;
    }

    /**
     * get a DocumentReference to this EntrantStatus in the database
     * @return
     */
    public DocumentReference getEntrantStatusReference() {
        return this.entrantStatusRef;
    }
}
