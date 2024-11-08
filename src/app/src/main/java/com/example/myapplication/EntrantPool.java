package com.example.myapplication;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EntrantPool {
    private ArrayList<EntrantStatus> entrants;
    private Event event;
    private FirebaseFirestore db;
    private DocumentReference eventRef;
    private CollectionReference entrantsPoolCol;

    /**
     * EntrantPool constructor, no users in the pool by default
     * @param event
     */
    public EntrantPool(Event event) {
        this.entrants = new ArrayList<EntrantStatus>();
        // TODO update database
        this.event = event;
        this.db = FirebaseFirestore.getInstance();
        this.eventRef = event.getEventRef();
        this.entrantsPoolCol = eventRef.collection("entrants");

    }

    /**
     * finds the EntrantStatus in the entrants list that contains the User/Entrant.
     * returns null if none was found
     * @param entrant
     * @return
     */
    @Nullable
    private EntrantStatus findEntrant(User entrant) {
        // find entrant status with matching entrant
        for (EntrantStatus entrantStatus : this.entrants) {
            if (entrantStatus.getEntrant() == entrant) {
                return entrantStatus; // found, return this one
            }
        }
        // we've now looped through the whole list and found no match
        return null;
    }

    /**
     * add an entrant to the pool with default status "none"
     * @param entrant
     * @param joinedFrom
     * @throws EntrantAlreadyInPool
     */
    public void addEntrant(User entrant, LatLng joinedFrom) throws EntrantAlreadyInPool {
        if (entrant == null) {
            return;
        }
        if (findEntrant(entrant) != null) {
            // entrant is already in this pool, they cannot be added again
            throw new EntrantAlreadyInPool("entrant cannot be added to pool that they are already in");
        }
        // joinedFrom can be null though
        EntrantStatus entrantStatus = new EntrantStatus(entrant, joinedFrom, this);
        this.entrants.add(entrantStatus);
    }

    /**
     * remove an entrant from the pool
     * @param entrant
     */
    public void removeEntrant(User entrant) {
        if (entrant == null) {
            return; // nothing to do here
        }
        EntrantStatus entrantStatus = findEntrant(entrant);
        if (entrantStatus == null) {
            return; // no entrant to remove
        }
        this.entrants.remove(entrantStatus);
        this.entrantsPoolCol.document(entrantStatus.getEntrantId()).delete(); // delete from entrants pool collection
    }

    /**
     * set an entrant's status, if they are not in the pool, nothing happens
     * @param entrant
     * @param status
     */
    public void setEntrantStatus(User entrant, Status status) {
        if (entrant == null) {
            return;
        }
        EntrantStatus entrantStatus = findEntrant(entrant);
        if (entrantStatus == null) {
            return; // no entrant to change status of
        }
        entrantStatus.setStatus(status);
        this.entrantsPoolCol.document(entrantStatus.getEntrantId()).update("status", status); // update status in database for entrant
    }

    /**
     * get a list of the entrants in the pool
     * @return
     */
    public ArrayList<User> getEntrants() {
        ArrayList<User> entrants = new ArrayList<User>();
        // add entrants from each entrant status into list
        for (EntrantStatus entrantStatus : this.entrants) {
            entrants.add(entrantStatus.getEntrant());
        }
        return entrants;
    }

    /**
     * get a list of the EntrantStatuses in the pool
     * @return
     */
    public ArrayList<EntrantStatus> getEntrantStatuses() {
        return this.entrants;
    }

    /**
     * draw a number of entrants from the pool and return that list
     * @param howMany
     * @return
     */
    public ArrayList<User> drawEntrants(int howMany) {
        // TODO implement this method
        // don't forget to update their statuses when drawing!
        return new ArrayList<User>(); // temporary
        // TODO update database
    }

    /**
     * get a DocumentReference to this EntrantPool in the database
     * @return
     */
    public CollectionReference getEntrantsPoolCol() {
        return entrantsPoolCol; // return reference to entrants pool collection in database
    }
}
