package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/*
This class is responsible for getting and setting an entrants status. It also allows organizers
to send notifications to entrants.
 */
public class EntrantStatus {
    private final User entrant;
    private final LatLng joinedFrom;
    private final EntrantPool entrantPool;
    private Status status;
    private FirebaseFirestore db;
    private DocumentReference entrantRef;
    private String entrantId;
    private HashMap<String, Object> entrantData;

    /**
     * Simplest constructor for an EntrantStatus.
     * Sets the entrant's status to "none", which means that no draw has occurred yet
     * @param entrant
     * @param joinedFrom
     * @param entrantPool
     */
    public EntrantStatus(User entrant, LatLng joinedFrom, EntrantPool entrantPool) {
        this.entrant = entrant;
        this.joinedFrom = joinedFrom;
        this.entrantPool = entrantPool;
        this.status = Status.none; // starting status is none (no draw has occurred yet)
        // TODO update database
        this.db = FirebaseFirestore.getInstance();
        this.entrantRef = entrantPool.getEntrantsPoolCol().document();
        this.entrantId = entrantRef.getId();
        this.entrantData.put("entrant", this.entrant);
        this.entrantData.put("joinedFrom", this.joinedFrom);
        this.entrantData.put("status", this.status);
        this.entrantRef.set(entrantData);

    }

    /**
     * The same as the simplest constructor but allows setting a specific status
     * @param entrant
     * @param joinedFrom
     * @param entrantPool
     * @param status
     */
    public EntrantStatus(User entrant, LatLng joinedFrom, EntrantPool entrantPool, Status status) {
        this(entrant, joinedFrom, entrantPool);
        this.status = status; // this constructor allows setting a different starting status
    }

    /**
     * sets this entrant's status
     * @param status
     */
    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
            entrantData.put("status", this.status);
            this.entrantRef.update(entrantData);
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

    /**
     * get a DocumentReference to this EntrantStatus in the database
     * @return
     */
    public DocumentReference getEntrantStatusReference() {
        // FIXME
    }
}
