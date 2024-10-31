package com.example.myapplication;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class EntrantPool {
    private ArrayList<EntrantStatus> entrants;

    public EntrantPool() {
        this.entrants = new ArrayList<EntrantStatus>();
        // TODO update database
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

    public void addEntrant(User entrant, LatLng joinedFrom) throws EntrantAlreadyInPool {
        if (entrant == null) {
            return;
        }
        if (findEntrant(entrant) != null) {
            // entrant is already in this pool, they cannot be added again
            throw new EntrantAlreadyInPool("entrant cannot be added to pool that they are already in");
        }
        // joinedFrom can be null though
        EntrantStatus entrantStatus = new EntrantStatus(entrant, joinedFrom);
        this.entrants.add(entrantStatus);
        // TODO update database
    }

    public void removeEntrant(User entrant) {
        if (entrant == null) {
            return; // nothing to do here
        }
        EntrantStatus entrantStatus = findEntrant(entrant);
        if (entrantStatus == null) {
            return; // no entrant to remove
        }
        this.entrants.remove(entrantStatus);
        // TODO update database
    }

    public void setEntrantStatus(User entrant, Status status) {
        if (entrant == null) {
            return;
        }
        EntrantStatus entrantStatus = findEntrant(entrant);
        if (entrantStatus == null) {
            return; // no entrant to change status of
        }
        entrantStatus.setStatus(status);
        // TODO update database
    }

    public ArrayList<User> getEntrants() {
        ArrayList<User> entrants = new ArrayList<User>();
        // add entrants from each entrant status into list
        for (EntrantStatus entrantStatus : this.entrants) {
            entrants.add(entrantStatus.getEntrant());
        }
        return entrants;
    }

    public ArrayList<EntrantStatus> getEntrantStatuses() {
        return this.entrants;
    }

    public ArrayList<User> drawEntrants(int howMany) {
        // TODO implement this method
        // don't forget to update their statuses when drawing!
        return new ArrayList<User>(); // temporary
        // TODO update database
    }
}
