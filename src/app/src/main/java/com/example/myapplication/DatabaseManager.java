package com.example.myapplication;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseManager { // static class
    private final FirebaseFirestore db;
    private ArrayList<User> users;
    // the ArrayList of users is intended to store a reference to each and every User object created by the getUser method.
    // The reason this is done is because the getUser method is recursive,
    // however, the getEntrantStatus method at the "end" of the recursion needs to call the getUser method.
    // without this ArrayList, copies of the same user may be created indefinitely,
    // however this method allows the infinite loop to be avoided by using
    // already existing instances of users we are looking for

    public DatabaseManager() {
        this.db = FirebaseFirestore.getInstance();
        this.users = new ArrayList<User>();
    }

    public DocumentReference createUser(User user) {
        if (user == null) {
            return null;
        }
        String userID = user.getUniqueID();
        HashMap<String, Object> userData = new HashMap<>();
        userData.put(DatabaseUserFieldNames.isAdmin.name(), user.isAdmin());
        userData.put(DatabaseUserFieldNames.name.name(), user.getName());
        userData.put(DatabaseUserFieldNames.email.name(), user.getEmail());
        userData.put(DatabaseUserFieldNames.phoneNumber.name(), user.getPhoneNumber());
        //userData.put(DatabaseUserFieldNames.profilePicture.name(), user.getProfilePicture());
        userData.put(DatabaseUserFieldNames.receivesOrgAdmNotifications.name(), user.getReceivesOrgAdmNotifications());
        DocumentReference userRef = this.db.collection(DatabaseCollectionNames.users.name()).document(userID);
        userRef.set(userData);

        return userRef;
    }

    public void updateUser(User user) {
        if (user == null) {
            return;
        }
        HashMap<String, Object> userData = new HashMap<>();
        userData.put(DatabaseUserFieldNames.name.name(), user.getName());
        userData.put(DatabaseUserFieldNames.email.name(), user.getEmail());
        userData.put(DatabaseUserFieldNames.phoneNumber.name(), user.getPhoneNumber());
        userData.put(DatabaseUserFieldNames.profilePicture.name(), user.getProfilePicture());
        userData.put(DatabaseUserFieldNames.receivesOrgAdmNotifications.name(), user.getReceivesOrgAdmNotifications());
        DocumentReference userRef = user.getUserReference();
        userRef.update(userData);
        this.updateFacility(user.getFacility());
    }

    public User getUser(String userID) throws InterruptedException, ExecutionException {
        // before creating a new user object from the database, we need to check if we already have a User object for this user
        for (User user : this.users) {
            if (user.getUniqueID().equals(userID)) {
                return user;
            }
        }

        // we didn't already have this user created, so we fetch it from the database
        DocumentReference userRef = this.db.collection(DatabaseCollectionNames.users.name()).document(userID);
        User user = null;

        Task<DocumentSnapshot> task = userRef.get();
        DocumentSnapshot documentSnapshot = Tasks.await(task); // FIXME can't call this on main thread
        if (documentSnapshot == null) {
            throw new UserDoesNotExist("documentSnapshot was null");
        }
        if (!documentSnapshot.exists()) {
            throw new UserDoesNotExist("documentSnapshot does not exist");
        }
        HashMap<String, Object> userData = (HashMap<String, Object>) documentSnapshot.getData();
        if (userData == null) {
            throw new UserDoesNotExist("this user does not exist in the database");
        }

        // get user data from document

        Object isAdminTemp = userData.get(DatabaseUserFieldNames.isAdmin.name());
        if (isAdminTemp == null) {
            throw new UserDoesNotExist("this user was missing the isAdmin field");
        }
        boolean isAdmin = (boolean) isAdminTemp;

        Object nameTemp = userData.get(DatabaseUserFieldNames.name.name());
        if (nameTemp == null) {
            throw new UserDoesNotExist("this user was missing the name field");
        }
        String name = (String) nameTemp;

        Object emailTemp = userData.get(DatabaseUserFieldNames.email.name());
        if (emailTemp == null) {
            throw new UserDoesNotExist("this user was missing the email field");
        }
        String email = (String) emailTemp;

        Object phoneNumberTemp = userData.get(DatabaseUserFieldNames.phoneNumber.name());
        if (phoneNumberTemp == null) {
            throw new UserDoesNotExist("this user was missing the phoneNumber field");
        }
        Long phoneNumber = (Long) phoneNumberTemp;

        Object profilePictureTemp = userData.get(DatabaseUserFieldNames.profilePicture.name());
        if (profilePictureTemp == null) {
            throw new UserDoesNotExist("this user was missing the profilePicture field");
        }
        Bitmap profilePicture = (Bitmap) profilePictureTemp;

        Object notificationTemp = userData.get(DatabaseUserFieldNames.receivesOrgAdmNotifications.name());
        if (notificationTemp == null) {
            throw new UserDoesNotExist("this user was missing the receivesOrgAdmNotifications field");
        }
        boolean receivesOrgAdmNotifications = (boolean) notificationTemp;

        try {
            user = new User(userID, name, email, phoneNumber, profilePicture, isAdmin, receivesOrgAdmNotifications, userRef, null);
        } catch (Exception e) {
            user = null;
            throw new RuntimeException(e);
        }

        if (user == null) {
            return null;
        }
        this.users.add(user);
        Facility facility = this.getFacility(user); // get user's facility
        user.setFacility(facility);

        return user;
    }

    public DocumentReference createFacility(User user, Facility facility) {
        if (user == null || facility == null) {
            return null;
        }
        DocumentReference userRef = user.getUserReference();
        DocumentReference facilityRef = userRef.collection(DatabaseCollectionNames.facilities.name()).document();
        HashMap<String, Object> facilityData = new HashMap<>();
        facilityData.put(DatabaseFacilityFieldNames.name.name(), facility.getName());
        facilityData.put(DatabaseFacilityFieldNames.location.name(), facility.getLocation());
        facilityRef.set(facilityData);

        return facilityRef;
    }

    public void updateFacility(Facility facility) {
        if (facility == null) {
            return;
        }
        HashMap<String, Object> facilityData = new HashMap<>();
        facilityData.put(DatabaseFacilityFieldNames.name.name(), facility.getName());
        facilityData.put(DatabaseFacilityFieldNames.location.name(), facility.getLocation());
        DocumentReference facilityRef = facility.getFacilityReference();
        facilityRef.update(facilityData);
        for (Event event : facility.getEvents()) {
            this.updateEvent(event);
        }
    }

    public Facility getFacility(User organizer) {
        DocumentReference userRef = organizer.getUserReference();
        CollectionReference facilityCol = userRef.collection(DatabaseCollectionNames.facilities.name());
        final Facility[] facility = new Facility[1];
        facility[0] = null;
        facilityCol.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                DocumentReference facilityRef;
                assert (documentSnapshots.size() <= 1); // user should have either 0 or 1 facility
                if (documentSnapshots.isEmpty()) {
                    return;
                }
                facilityRef = documentSnapshots.get(0).getReference();
                HashMap<String, Object> facilityData = (HashMap<String, Object>) documentSnapshots.get(0).getData();
                if (facilityData == null) {
                    return;
                }

                // get facility data from document

                Object nameTemp = facilityData.get(DatabaseFacilityFieldNames.name.name());
                if (nameTemp == null) {
                    throw new FacilityDoesNotExist("this facility was missing the name field");
                }
                String name = (String) nameTemp;

                Object locationTemp = facilityData.get(DatabaseFacilityFieldNames.location.name());
                if (locationTemp == null) {
                    throw new FacilityDoesNotExist("this facility was missing the location field");
                }
                LatLng location = (LatLng) locationTemp;

                try {
                    facility[0] = new Facility(name, location, facilityRef, new ArrayList<Event>());
                }
                catch (Exception e) {
                    facility[0] = null;
                    throw new RuntimeException(e);
                }
            }
        });
        if (facility[0] == null) {
            return null;
        }
        ArrayList<Event> events = this.getEvents(facility[0]); // get facility's events
        for (Event event : events) {
            facility[0].addEvent(event);
        }

        return facility[0];
    }

    public DocumentReference createEvent(Facility facility, Event event) {
        if (facility == null || event == null) {
            return null;
        }
        DocumentReference facilityRef = facility.getFacilityReference();
        DocumentReference eventRef = facilityRef.collection(DatabaseCollectionNames.events.name()).document();
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put(DatabaseEventFieldNames.name.name(), event.getName());
        eventData.put(DatabaseEventFieldNames.date.name(), event.getDate());
        eventData.put(DatabaseEventFieldNames.eventPoster.name(), event.getEventPoster());
        eventData.put(DatabaseEventFieldNames.qrCode.name(), event.getQrCode().getText());
        eventData.put(DatabaseEventFieldNames.capacity.name(), event.getCapacity());
        eventRef.set(eventData);

        return eventRef;
    }

    public void updateEvent(Event event) {
        if (event == null) {
            return;
        }
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put(DatabaseEventFieldNames.name.name(), event.getName());
        eventData.put(DatabaseEventFieldNames.date.name(), event.getDate());
        eventData.put(DatabaseEventFieldNames.eventPoster.name(), event.getEventPoster());
        eventData.put(DatabaseEventFieldNames.qrCode.name(), event.getQrCode());
        eventData.put(DatabaseEventFieldNames.capacity.name(), event.getCapacity());
        DocumentReference eventRef = event.getEventReference();
        eventRef.update(eventData);
        for (EntrantStatus entrantStatus : event.getEntrantStatuses()) {
            this.updateEntrantStatus(entrantStatus);
        }
    }

    public ArrayList<Event> getEvents(Facility facility) {
        DocumentReference facilityRef = facility.getFacilityReference();
        CollectionReference eventCol = facilityRef.collection(DatabaseCollectionNames.events.name());
        ArrayList<Event> events = new ArrayList<>();
        eventCol.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                ArrayList<DocumentReference> eventRefs = new ArrayList<>();
                if (documentSnapshots.isEmpty()) {
                    return;
                }
                HashMap<String, Object> eventData;

                for (DocumentSnapshot documentSnapshot : documentSnapshots) { // for each event is the collection
                    eventData = (HashMap<String, Object>) documentSnapshot.getData();
                    if (eventData == null) {
                        continue;
                    }
                    eventRefs.add(documentSnapshot.getReference());

                    // get event data from document

                    Object nameTemp = eventData.get(DatabaseEventFieldNames.name.name());
                    if (nameTemp == null) {
                        throw new EventDoesNotExist("this event was missing the name field");
                    }
                    String name = (String) nameTemp;

                    Object dateTemp = eventData.get(DatabaseEventFieldNames.date.name());
                    if (dateTemp == null) {
                        throw new EventDoesNotExist("this event was missing the date field");
                    }
                    Date date = (Date) dateTemp;

                    Object eventPosterTemp = eventData.get(DatabaseEventFieldNames.eventPoster.name());
                    if (eventPosterTemp == null) {
                        throw new EventDoesNotExist("this event was missing the eventPoster field");
                    }
                    Bitmap eventPoster = (Bitmap) eventPosterTemp;

                    Object qrCodeTemp = eventData.get(DatabaseEventFieldNames.qrCode.name());
                    if (qrCodeTemp == null) {
                        throw new EventDoesNotExist("this event was missing the qrCode field");
                    }
                    QRCode qrCode = new QRCode((String) qrCodeTemp);

                    Object capacityTemp = eventData.get(DatabaseEventFieldNames.capacity.name());
                    if (capacityTemp == null) {
                        throw new EventDoesNotExist("this event was missing the capacity field");
                    }
                    Integer capacity = (Integer) capacityTemp;

                    try {
                        events.add(new Event(name, date, eventPoster, capacity, qrCode, null, eventRefs.get(eventRefs.size()-1)));
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        for (Event event : events) {
            ArrayList<EntrantStatus> entrantStatuses = this.getEntrantStatuses(event);
            EntrantPool entrantPool = new EntrantPool();
            for (EntrantStatus entrantStatus : entrantStatuses) {
                entrantPool.addEntrant(entrantStatus.getEntrant(), entrantStatus.getJoinedFrom(), entrantStatus.getStatus());
            }
            // recreate event with EntrantPool which was previously missing
            events.remove(event);
            try {
                event = new Event(event.getName(), event.getDate(), event.getEventPoster(), event.getCapacity(), event.getQrCode(), entrantPool, event.getEventReference());
            }
            catch (Exception e) {
                continue;
            }
            events.add(event);
        }

        return events;
    }

    public DocumentReference createEntrantStatus(Event event, EntrantStatus entrantStatus) {
        if (event == null || entrantStatus == null) {
            return null;
        }
        DocumentReference eventRef = event.getEventReference();
        DocumentReference entrantStatusRef = eventRef.collection(DatabaseCollectionNames.entrantStatuses.name()).document();
        HashMap<String, Object> entrantStatusData = new HashMap<>();
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.entrantID.name(), entrantStatus.getEntrant().getUniqueID());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.joinedFrom.name(), entrantStatus.getJoinedFrom());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.status.name(), entrantStatus.getStatus());
        entrantStatusRef.set(entrantStatusData);

        return entrantStatusRef;
    }

    public void updateEntrantStatus(EntrantStatus entrantStatus) {
        if (entrantStatus == null) {
            return;
        }
        HashMap<String, Object> entrantStatusData = new HashMap<>();
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.entrantID.name(), entrantStatus.getEntrant().getUniqueID());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.joinedFrom.name(), entrantStatus.getJoinedFrom());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.status.name(), entrantStatus.getStatus());
        DocumentReference entrantStatusRef = entrantStatus.getEntrantStatusReference();
        entrantStatusRef.update(entrantStatusData);
    }

    public ArrayList<EntrantStatus> getEntrantStatuses(Event event) {
        DocumentReference eventRef = event.getEventReference();
        CollectionReference entrantStatusCol = eventRef.collection(DatabaseCollectionNames.entrantStatuses.name());
        ArrayList<EntrantStatus> entrantStatuses = new ArrayList<>();
        entrantStatusCol.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                ArrayList<DocumentReference> entrantStatusRefs = new ArrayList<>();
                if (documentSnapshots.isEmpty()) {
                    return;
                }
                HashMap<String, Object> entrantStatusData;

                for (DocumentSnapshot documentSnapshot : documentSnapshots) { // for each entrantStatus in collection
                    entrantStatusData = (HashMap<String, Object>) documentSnapshot.getData();
                    if (entrantStatusData == null) {
                        continue;
                    }
                    entrantStatusRefs.add(documentSnapshot.getReference());

                    // get entrantStatus data from document

                    Object entrantIDTemp = entrantStatusData.get(DatabaseEntrantStatusFieldNames.entrantID.name());
                    if (entrantIDTemp == null) {
                        throw new EntrantStatusDoesNotExist("this entrantstatus was missing the entrantID field");
                    }
                    String entrantID = (String) entrantIDTemp;

                    Object joinedFromTemp = entrantStatusData.get(DatabaseEntrantStatusFieldNames.joinedFrom.name());
                    if (joinedFromTemp == null) {
                        throw new EntrantStatusDoesNotExist("this entrantstatus was missing the joinedFrom field");
                    }
                    LatLng joinedFrom = (LatLng) joinedFromTemp;

                    Object statusTemp = entrantStatusData.get(DatabaseEntrantStatusFieldNames.status.name());
                    if (statusTemp == null) {
                        throw new EntrantStatusDoesNotExist("this entrantstatus was missing the status field");
                    }
                    Status status = (Status) statusTemp;

                    try {
                        User entrant = getUser(entrantID);
                        entrantStatuses.add(new EntrantStatus(entrant, joinedFrom, status, entrantStatusRefs.get(entrantStatusRefs.size()-1)));
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return entrantStatuses;
    }
}
