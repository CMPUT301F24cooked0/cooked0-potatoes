package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EntrantStatus {
    private final User entrant;
    private final LatLng joinedFrom;
    private final EntrantPool entrantPool;
    private Status status;
    private FirebaseFirestore db;
    private DocumentReference entrantRef;
    private String entrantId;
    private HashMap<String, Object> entrantData;


    public EntrantStatus(User entrant, LatLng joinedFrom, EntrantPool entrantPool, FirebaseFirestore db) {
        this.entrant = entrant;
        this.joinedFrom = joinedFrom;
        this.entrantPool = entrantPool;
        this.status = Status.none; // starting status is none (no draw has occurred yet)
        // TODO update database
        this.db = db;
        this.entrantRef = entrantPool.getEntrantsPoolCol().document();
        this.entrantId = entrantRef.getId();
        this.entrantData.put("entrant", this.entrant);
        this.entrantData.put("joinedFrom", this.joinedFrom);
        this.entrantData.put("status", this.status);
        this.entrantRef.set(entrantData);
    }

    public EntrantStatus(User entrant, LatLng joinedFrom, EntrantPool entrantPool, Status status, FirebaseFirestore db) {
        this(entrant, joinedFrom, entrantPool, db);
        this.status = status; // this constructor allows setting a different starting status
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
            entrantData.put("status", this.status);
            this.entrantRef.update(entrantData);
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

    public String getEntrantId() {
        return entrantId; // return document id of entrant in database
    }
}
