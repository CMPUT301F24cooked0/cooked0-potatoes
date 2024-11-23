package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseManager implements OnFacilityFetchListener, OnEventsFetchListener, OnEntrantStatusesFetchListener { // static class
    private final FirebaseFirestore db;
    private ArrayList<User> users;
    // the ArrayList of users is intended to store a reference to each and every User object created by the getUser method.
    // The reason this is done is because the getUser method is recursive,
    // however, the getEntrantStatus method at the "end" of the recursion needs to call the getUser method.
    // without this ArrayList, copies of the same user may be created indefinitely,
    // however this method allows the infinite loop to be avoided by using
    // already existing instances of users we are looking for

    /**
     * Connects to the database
     */
    public DatabaseManager() {
        this.db = FirebaseFirestore.getInstance();
        this.users = new ArrayList<User>();
    }

    /**
     * Inserts a User into the database.
     * Recursively inserts all of the objects attached to the user (Facility, Events, EntrantStatuses)
     * @param user
     * @return true on success, false on failure or if user was null
     */
    public Boolean createUser(User user) {
        if (user == null) {
            return false;
        }
        String userID = user.getUniqueID();
        HashMap<String, Object> userData = new HashMap<>();
        userData.put(DatabaseUserFieldNames.isAdmin.name(), user.isAdmin());
        userData.put(DatabaseUserFieldNames.name.name(), user.getName());
        userData.put(DatabaseUserFieldNames.email.name(), user.getEmail());
        userData.put(DatabaseUserFieldNames.phoneNumber.name(), user.getPhoneNumber());
        //userData.put(DatabaseUserFieldNames.profilePicture.name(), user.getProfilePicture()); // FIXME this causes issues
        userData.put(DatabaseUserFieldNames.receivesOrgAdmNotifications.name(), user.getReceivesOrgAdmNotifications());
        DocumentReference userRef = this.db.collection(DatabaseCollectionNames.users.name()).document(userID);
        userRef.set(userData);
        user.setUserReference(userRef);
        this.createFacility(user, user.getFacility());

        return true;
    }

    /**
     * Updates the information related to a User in the database.
     * Recursively updates all of the objects attached to the user (Facility, Events, EntrantStatuses)
     * @param user
     */
    public void updateUser(User user) {
        if (user == null || user.getUserReference() == null) {
            return;
        }
        HashMap<String, Object> userData = new HashMap<>();
        userData.put(DatabaseUserFieldNames.name.name(), user.getName());
        userData.put(DatabaseUserFieldNames.email.name(), user.getEmail());
        userData.put(DatabaseUserFieldNames.phoneNumber.name(), user.getPhoneNumber());
        //userData.put(DatabaseUserFieldNames.profilePicture.name(), user.getProfilePicture()); // FIXME
        userData.put(DatabaseUserFieldNames.receivesOrgAdmNotifications.name(), user.getReceivesOrgAdmNotifications());
        DocumentReference userRef = user.getUserReference();
        userRef.update(userData);
        this.updateFacility(user.getFacility());
    }

    /**
     * Requests to get a User from the database.
     * Once the User is fetched, which is done asynchronously, it will be returned
     * via the onUserFetchListener method.
     * IMPORTANT NOTE: The DatabaseManager will recursively build the User
     * and attach all objects that the User is attached to (its Facility, Events, EntrantStatuses),
     * however this MAY be done after the onUserFetchListener has returned the user // FIXME get things recursively all on the same thread in private methods, so that the user object is fully built before being returned
     * @param userID
     * @param onUserFetchListener
     */
    public void getUser(String userID, OnUserFetchListener onUserFetchListener) {
        Thread thread = new Thread(() -> {
            User user = fetchUser(userID);
            onUserFetchListener.onUserFetch(user);
        });
        thread.start();
    }

    private User fetchUser(String userID) {
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
        DocumentSnapshot documentSnapshot = null;
        try {
            documentSnapshot = Tasks.await(task);
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }

        if (documentSnapshot == null) {
            return null;
        }
        if (!documentSnapshot.exists()) {
            return null;
        }
        HashMap<String, Object> userData = (HashMap<String, Object>) documentSnapshot.getData();
        if (userData == null) {
            return null;
        }

        // get user data from document
        try {
            Object isAdminTemp = userData.get(DatabaseUserFieldNames.isAdmin.name());
            boolean isAdmin = (boolean) isAdminTemp;

            Object nameTemp = userData.get(DatabaseUserFieldNames.name.name());
            if (nameTemp == null) {
                // user name is mandatory
                throw new UserDoesNotExist("this user was missing the name field");
            }
            String name = (String) nameTemp;

            Object emailTemp = userData.get(DatabaseUserFieldNames.email.name());
            if (emailTemp == null) {
                // user email is mandatory
                throw new UserDoesNotExist("this user was missing the email field");
            }
            String email = (String) emailTemp;

            Object phoneNumberTemp = userData.get(DatabaseUserFieldNames.phoneNumber.name());
            Long phoneNumber = (Long) phoneNumberTemp;

            Object profilePictureTemp = userData.get(DatabaseUserFieldNames.profilePicture.name());
            Bitmap profilePicture = (Bitmap) profilePictureTemp;

            Object notificationTemp = userData.get(DatabaseUserFieldNames.receivesOrgAdmNotifications.name());
            boolean receivesOrgAdmNotifications = (boolean) notificationTemp;

            try {
                user = new User(userID, name, email, phoneNumber, profilePicture, isAdmin, receivesOrgAdmNotifications, userRef, null);
            } catch (Exception e) {
                user = null;
                throw new RuntimeException(e);
            }
        }
        catch (Exception e) {
            user = null;
        }

        if (user == null) {
            return null;
        }
        this.users.add(user);
        this.getFacility(user, this); // get user's facility, which is automatically added to user

        return user;
    }

    /**
     * Inserts a Facility into the database.
     * Recursively inserts all of the objects attached to the Facility (Events, EntrantStatuses)
     * @param user
     * @param facility
     * @return true on success, false on failure or if either user or facility was null
     */
    public Boolean createFacility(User user, Facility facility) {
        if (user == null || facility == null) {
            return false;
        }
        DocumentReference userRef = user.getUserReference();
        DocumentReference facilityRef = userRef.collection(DatabaseCollectionNames.facilities.name()).document();
        HashMap<String, Object> facilityData = new HashMap<>();
        facilityData.put(DatabaseFacilityFieldNames.name.name(), facility.getName());
        facilityData.put(DatabaseFacilityFieldNames.location.name(), facility.getLocation());
        facilityRef.set(facilityData);
        facility.setFacilityReference(facilityRef);
        for (Event event : facility.getEvents()) {
            this.createEvent(facility, event);
        }

        return true;
    }

    /**
     * Updates the information related to a Facility in the database.
     * Recursively updates all of the objects attached to the facility (Events, EntrantStatuses)
     * @param facility
     */
    public void updateFacility(Facility facility) {
        if (facility == null || facility.getFacilityReference() == null) {
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

    /**
     * Requests to get a User's Facility from the database.
     * Once the Facility is fetched, which is done asynchronously, it will be returned
     * via the onFacilityFetchListener method.
     * @param organizer
     * @param onFacilityFetchListener
     */
    public void getFacility(User organizer, OnFacilityFetchListener onFacilityFetchListener) {
        Thread thread = new Thread(() -> {
            Facility facility = fetchFacility(organizer);
            onFacilityFetchListener.onFacilityFetch(organizer, facility);
        });
        thread.start();
    }

    private Facility fetchFacility(User organizer) {
        DocumentReference userRef = organizer.getUserReference();
        CollectionReference facilityCol = userRef.collection(DatabaseCollectionNames.facilities.name());
        Facility facility = null;

        Task<QuerySnapshot> task = facilityCol.get();
        QuerySnapshot queryDocumentSnapshots = null;
        try {
            queryDocumentSnapshots = Tasks.await(task);
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }

        if (queryDocumentSnapshots == null) {
            return null;
        }
        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
        DocumentReference facilityRef;
        assert (documentSnapshots.size() <= 1); // user should have either 0 or 1 facility
        if (documentSnapshots.isEmpty()) {
            return null;
        }
        facilityRef = documentSnapshots.get(0).getReference();
        HashMap<String, Object> facilityData = (HashMap<String, Object>) documentSnapshots.get(0).getData();
        if (facilityData == null) {
            return null;
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
        HashMap<String, Double> locationMap = (HashMap<String, Double>) locationTemp;
        LatLng location = new LatLng(locationMap.get("latitude"), locationMap.get("longitude"));

        try {
            facility = new Facility(name, location, facilityRef, new ArrayList<Event>());
        }
        catch (Exception e) {
            facility = null;
            throw new RuntimeException(e);
        }
        this.getEvents(facility, this); // get facility's events

        return facility;
    }

    @Override
    public void onFacilityFetch(User organizer, Facility facility) {
        organizer.setFacility(facility);
    }

    /**
     * Inserts an Event into the database.
     * Recursively inserts all of the objects attached to the event (EntrantStatuses)
     * @param facility
     * @param event
     * @return true on success, false on failure or if either facility or event was null
     */
    public Boolean createEvent(Facility facility, Event event) {
        if (facility == null || event == null) {
            return false;
        }
        DocumentReference facilityRef = facility.getFacilityReference();
        DocumentReference eventRef = facilityRef.collection(DatabaseCollectionNames.events.name()).document();
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put(DatabaseEventFieldNames.name.name(), event.getName());
        eventData.put(DatabaseEventFieldNames.instant.name(), new Timestamp(event.getInstant()));
        //eventData.put(DatabaseEventFieldNames.eventPoster.name(), event.getEventPoster()); // FIXME image
        eventData.put(DatabaseEventFieldNames.qrCode.name(), event.getQrCode().getText());
        eventData.put(DatabaseEventFieldNames.capacity.name(), event.getCapacity());
        eventRef.set(eventData);
        event.setEventReference(eventRef);
        for (EntrantStatus entrantStatus : event.getEntrantStatuses()) {
            this.createEntrantStatus(event, entrantStatus);
        }

        return true;
    }

    /**
     * Updates the information related to an Event in the database.
     * Recursively updates all of the objects attached to the event (EntrantStatuses)
     * @param event
     */
    public void updateEvent(Event event) {
        if (event == null || event.getEventReference() == null) {
            return;
        }
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put(DatabaseEventFieldNames.name.name(), event.getName());
        eventData.put(DatabaseEventFieldNames.instant.name(), new Timestamp(event.getInstant()));
        //eventData.put(DatabaseEventFieldNames.eventPoster.name(), event.getEventPoster()); // FIXME
        eventData.put(DatabaseEventFieldNames.qrCode.name(), event.getQrCode().getText());
        eventData.put(DatabaseEventFieldNames.capacity.name(), event.getCapacity());
        DocumentReference eventRef = event.getEventReference();
        eventRef.update(eventData);
        for (EntrantStatus entrantStatus : event.getEntrantStatuses()) {
            this.updateEntrantStatus(entrantStatus);
        }
    }

    /**
     * Requests to get a Facility's Events from the database.
     * Once the Events have all been fetched, which is done asynchronously, they wil be returned
     * via the onEventsFetchListener method.
     * @param facility
     * @param onEventsFetchListener
     */
    public void getEvents(Facility facility, OnEventsFetchListener onEventsFetchListener) {
        Thread thread = new Thread(() -> {
            ArrayList<Event> events = fetchEvents(facility);
            onEventsFetchListener.onEventsFetch(facility, events);
        });
        thread.start();
    }

    private ArrayList<Event> fetchEvents(Facility facility) {
        DocumentReference facilityRef = facility.getFacilityReference();
        CollectionReference eventCol = facilityRef.collection(DatabaseCollectionNames.events.name());
        ArrayList<Event> events = new ArrayList<>();

        Task<QuerySnapshot> task = eventCol.get();
        QuerySnapshot queryDocumentSnapshots = null;
        try {
            queryDocumentSnapshots = Tasks.await(task);
        } catch (ExecutionException e) {
            return events;
        } catch (InterruptedException e) {
            return events;
        }

        if (queryDocumentSnapshots == null) {
            return events;
        }

        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
        ArrayList<DocumentReference> eventRefs = new ArrayList<>();
        if (documentSnapshots.isEmpty()) {
            return events;
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

            Object dateTemp = eventData.get(DatabaseEventFieldNames.instant.name());
            if (dateTemp == null) {
                throw new EventDoesNotExist("this event was missing the instant field");
            }
            Timestamp dateTimestamp = (Timestamp) dateTemp;
            Instant instant = dateTimestamp.toInstant();

            Object eventPosterTemp = eventData.get(DatabaseEventFieldNames.eventPoster.name());
            //if (eventPosterTemp == null) { // FIXME temp
            //    throw new EventDoesNotExist("this event was missing the eventPoster field");
            //}
            Bitmap eventPoster = (Bitmap) eventPosterTemp;

            Object qrCodeTemp = eventData.get(DatabaseEventFieldNames.qrCode.name());
            //if (qrCodeTemp == null) { // FIXME temp
            //    throw new EventDoesNotExist("this event was missing the qrCode field");
            //}
            QRCode qrCode = new QRCode((String) qrCodeTemp);

            Object capacityTemp = eventData.get(DatabaseEventFieldNames.capacity.name());
            Integer capacity = (Integer) capacityTemp;

            try {
                events.add(new Event(name, instant, eventPoster, capacity, qrCode, new EntrantPool(), eventRefs.get(eventRefs.size()-1)));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (Event event : events) {
            this.getEntrantStatuses(event, this);
        }

        return events;
    }

    @Override
    public void onEventsFetch(Facility facility, ArrayList<Event> events) {
        for (Event event : events) {
            facility.addEvent(event);
        }
    }

    /**
     * Inserts an EntrantStatus into the database.
     * @param event
     * @param entrantStatus
     * @return true on success, false on failure or if either event or entrantStatus was null
     */
    public Boolean createEntrantStatus(Event event, EntrantStatus entrantStatus) {
        if (event == null || entrantStatus == null) {
            return false;
        }
        DocumentReference eventRef = event.getEventReference();
        DocumentReference entrantStatusRef = eventRef.collection(DatabaseCollectionNames.entrantStatuses.name()).document();
        HashMap<String, Object> entrantStatusData = new HashMap<>();
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.entrantID.name(), entrantStatus.getEntrant().getUniqueID());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.joinedFrom.name(), entrantStatus.getJoinedFrom());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.status.name(), entrantStatus.getStatus());
        entrantStatusRef.set(entrantStatusData);
        entrantStatus.setEntrantStatusReference(entrantStatusRef);

        return true;
    }

    /**
     * Updates the information related to an EntrantStatus in the database.
     * @param entrantStatus
     */
    public void updateEntrantStatus(EntrantStatus entrantStatus) {
        if (entrantStatus == null || entrantStatus.getEntrantStatusReference() == null) {
            return;
        }
        HashMap<String, Object> entrantStatusData = new HashMap<>();
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.entrantID.name(), entrantStatus.getEntrant().getUniqueID());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.joinedFrom.name(), entrantStatus.getJoinedFrom());
        entrantStatusData.put(DatabaseEntrantStatusFieldNames.status.name(), entrantStatus.getStatus());
        DocumentReference entrantStatusRef = entrantStatus.getEntrantStatusReference();
        entrantStatusRef.update(entrantStatusData);
    }

    /**
     * Requests to get an Event's EntrantStatuses from the database.
     * Once the EntrantStatuses have all been fetched, which is done asynchronously, they will be returned
     * via the onEntrantStatusesFetchListener method.
     * @param event
     * @param onEntrantStatusesFetchListener
     */
    public void getEntrantStatuses(Event event, OnEntrantStatusesFetchListener onEntrantStatusesFetchListener) {
        Thread thread = new Thread(() -> {
            ArrayList<EntrantStatus> entrantStatuses = fetchEntrantStatuses(event);
            onEntrantStatusesFetchListener.onEntrantStatusesFetch(event, entrantStatuses);
        });
        thread.start();
    }

    private ArrayList<EntrantStatus> fetchEntrantStatuses(Event event) {
        DocumentReference eventRef = event.getEventReference();
        CollectionReference entrantStatusCol = eventRef.collection(DatabaseCollectionNames.entrantStatuses.name());
        ArrayList<EntrantStatus> entrantStatuses = new ArrayList<>();

        Task<QuerySnapshot> task = entrantStatusCol.get();
        QuerySnapshot queryDocumentSnapshots = null;
        try {
            queryDocumentSnapshots = Tasks.await(task);
        } catch (ExecutionException e) {
            return entrantStatuses;
        } catch (InterruptedException e) {
            return entrantStatuses;
        }

        if (queryDocumentSnapshots == null) {
            return entrantStatuses;
        }

        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
        ArrayList<DocumentReference> entrantStatusRefs = new ArrayList<>();
        if (documentSnapshots.isEmpty()) {
            return entrantStatuses;
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
            HashMap<String, Double> joinedFromMap = (HashMap<String, Double>) joinedFromTemp;
            LatLng joinedFrom = new LatLng(joinedFromMap.get("latitude"), joinedFromMap.get("longitude"));

            Object statusTemp = entrantStatusData.get(DatabaseEntrantStatusFieldNames.status.name());
            if (statusTemp == null) {
                throw new EntrantStatusDoesNotExist("this entrantstatus was missing the status field");
            }
            String statusString = (String) statusTemp;
            Status status = Status.valueOf(statusString);

            try {
                User entrant = fetchUser(entrantID);
                entrantStatuses.add(new EntrantStatus(entrant, joinedFrom, status, entrantStatusRefs.get(entrantStatusRefs.size()-1)));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return entrantStatuses;
    }

    @Override
    public void onEntrantStatusesFetch(Event event, ArrayList<EntrantStatus> entrantStatuses) {
        for (EntrantStatus entrantStatus : entrantStatuses) {
            event.addEntrant(entrantStatus.getEntrant(), entrantStatus.getJoinedFrom(), entrantStatus.getStatus());
        }
    }

    /**
     * Creates a new notification for the user with userID in the database.
     * This will be received by the user at some later time.
     * @param notification
     * @return true on success, false on failure or if either userID or notificationText is null
     */
    public Boolean createNotification(Notification notification) {
        Instant instantPosted = notification.getInstantPosted();
        HashMap<String, Object> notificationData = new HashMap<>();
        notificationData.put(DatabaseNotificationFieldNames.userID.name(), notification.getUserID());
        notificationData.put(DatabaseNotificationFieldNames.notificationText.name(), notification.getNotificationText());
        notificationData.put(DatabaseNotificationFieldNames.instantPosted.name(), new Timestamp(instantPosted));
        DocumentReference notificationRef = this.db.collection(DatabaseCollectionNames.notifications.name()).document();
        notificationRef.set(notificationData);

        return true;
    }

    public void getNotifications(String userID, OnNotificationFetchListener onNotificationFetchListener) {
        Thread thread = new Thread(() -> {
           ArrayList<Notification> notifications = fetchNotifications(userID);
           onNotificationFetchListener.onNotificationFetch(notifications);
        });
        thread.start();
    }

    private ArrayList<Notification> fetchNotifications(String userID) {
        CollectionReference notificationCol = this.db.collection(DatabaseCollectionNames.notifications.name());

    }
}
