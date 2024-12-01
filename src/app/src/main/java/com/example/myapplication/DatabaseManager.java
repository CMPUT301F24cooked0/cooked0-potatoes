package com.example.myapplication;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DatabaseManager implements OnFacilityFetchListener, OnEventsFetchListener, OnEntrantStatusesFetchListener { // static class
    private final FirebaseFirestore db;
    private ArrayList<User> users;
    private int stringMaximumLength = 1000000; // 1MB
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
        String encodedProfilePicture = null;
        try {
            encodedProfilePicture = BitmapConverter.BitmapToCompressedString(user.getProfilePicture(), this.stringMaximumLength);
        } catch (Exception e) {
            // the image could not be compressed small enough for some reason, so we unfortunately have to upload nothing.
            // this really should not be possible though
            encodedProfilePicture = null;
        }
        userData.put(DatabaseUserFieldNames.profilePicture.name(), encodedProfilePicture);
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
        String encodedProfilePicture = null;
        try {
            encodedProfilePicture = BitmapConverter.BitmapToCompressedString(user.getProfilePicture(), this.stringMaximumLength);
        } catch (Exception e) {
            // the image could not be compressed small enough for some reason, so we unfortunately have to upload nothing.
            // this really should not be possible though
            encodedProfilePicture = null;
        }
        userData.put(DatabaseUserFieldNames.profilePicture.name(), encodedProfilePicture);
        userData.put(DatabaseUserFieldNames.receivesOrgAdmNotifications.name(), user.getReceivesOrgAdmNotifications());
        DocumentReference userRef = user.getUserReference();
        userRef.update(userData);
        this.updateFacility(user.getFacility());
    }

    private Boolean deleteUserNoThread(User user) {
        if (user == null || user.getUserReference() == null) {
            return false;
        }
        this.deleteFacilityNoThread(user.getFacility());
        DocumentReference userRef = user.getUserReference();
        Task task = userRef.delete();
        try {
            Tasks.await(task);
        } catch (ExecutionException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Deletes the User (and everything about them, such as their facility, events, etc) in the database.
     * @param user
     * @return true if User was deleted, false if User could not be found or if they could not be deleted.
     */
    public Boolean deleteUser(User user) {
        if (user == null || user.getUserReference() == null) {
            return false;
        }
        Thread thread = new Thread(() -> {
            this.deleteUserNoThread(user);
        });
        thread.start();
        return true;
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

    /**
     * Requests to get all Users from the database.
     * Once all Users are fetched, which is done asynchronously, they will be returned
     * via the onAllUsersFetchListener method.
     * IMPORTANT NOTE: The DatabaseManager will recursively build the Users
     * and attach all objects that those Users are attached to (their Facility, Events, EntrantStatuses),
     * however this MAY be done after the onAllUsersFetchListener has returned the users
     * @param onAllUsersFetchListener
     */
    public void getAllUsers(OnAllUsersFetchListener onAllUsersFetchListener) {
        Thread thread = new Thread(() -> {
            ArrayList<User> users = fetchAllUsers();
            onAllUsersFetchListener.onAllUsersFetch(users);
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
            String encodedProfilePicture = (String) profilePictureTemp;
            Bitmap profilePicture = BitmapConverter.StringToBitmap(encodedProfilePicture);

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

    private ArrayList<User> fetchAllUsers() {
        ArrayList<User> users = new ArrayList<User>(this.users);

        CollectionReference userCol = this.db.collection(DatabaseCollectionNames.users.name());

        Task<QuerySnapshot> task = userCol.get();
        QuerySnapshot queryDocumentSnapshots = null;
        try {
            queryDocumentSnapshots = Tasks.await(task);
        } catch (ExecutionException e) {
            return users;
        } catch (InterruptedException e) {
            return users;
        }

        if (queryDocumentSnapshots == null) {
            return users;
        }

        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
        if (documentSnapshots.isEmpty()) {
            return users;
        }
        HashMap<String, Object> userData;
        DocumentReference userRef;
        String userID;

        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
            userData = (HashMap<String, Object>) documentSnapshot.getData();
            if (userData == null) {
                continue;
            }
            userRef = documentSnapshot.getReference();
            userID = userRef.getId();

            users.add(this.fetchUser(userID));
        }

        return users;
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
        facilityData.put(DatabaseFacilityFieldNames.address.name(), facility.getAddress());
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
        facilityData.put(DatabaseFacilityFieldNames.address.name(), facility.getAddress());
        DocumentReference facilityRef = facility.getFacilityReference();
        facilityRef.update(facilityData);
        for (Event event : facility.getEvents()) {
            this.updateEvent(event);
        }
    }

    private Boolean deleteFacilityNoThread(Facility facility) {
        if (facility == null || facility.getFacilityReference() == null) {
            return false;
        }
        for (Event event : facility.getEvents()) {
            this.deleteEventNoThread(event);
        }
        DocumentReference facilityRef = facility.getFacilityReference();
        Task task = facilityRef.delete();
        try {
            Tasks.await(task);
        } catch (ExecutionException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Deletes the Facility (and everything about them, such as events, etc) in the database.
     * @param facility
     * @return true if Facility was deleted, false if Facility could not be found or if it could not be deleted.
     */
    public Boolean deleteFacility(Facility facility) {
        if (facility == null || facility.getFacilityReference() == null) {
            return false;
        }
        Thread thread = new Thread(() -> {
            this.deleteFacilityNoThread(facility);
        });
        thread.start();
        return true;
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

    /**
     * Requests to get all Facilities from the database.
     * Once the facilities are fetched, which is done asynchronously, they will be returned
     * via the onAllFacilitiesFetchListener method.
     * @param onAllFacilitiesFetchListener
     */
    public void getAllFacilities(OnAllFacilitiesFetchListener onAllFacilitiesFetchListener) {
        Thread thread = new Thread(() -> {
            ArrayList<Facility> facilities = fetchAllFacilities();
            onAllFacilitiesFetchListener.onAllFacilitiesFetch(facilities);
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

        Object addressTemp = facilityData.get(DatabaseFacilityFieldNames.name.name());
        String address = (String) addressTemp;

        try {
            facility = new Facility(name, location, address, facilityRef, new ArrayList<Event>());
        }
        catch (Exception e) {
            facility = null;
            throw new RuntimeException(e);
        }
        this.getEvents(facility, this); // get facility's events

        return facility;
    }

    private ArrayList<Facility> fetchAllFacilities() {
        ArrayList<User> users = this.fetchAllUsers();
        ArrayList<Facility> facilities = new ArrayList<Facility>();

        Facility facility;
        for (User user : users) {
            facility = this.fetchFacility(user); // FIXME convert this to user.getFacility() once things can run on a single thread
            if (facility != null) {
                assert !facilities.contains(facility);
                facilities.add(facility);
            }
        }

        return facilities;
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
        eventData.put(DatabaseEventFieldNames.description.name(), event.getDescription());
        eventData.put(DatabaseEventFieldNames.geolocationRequired.name(), event.getGeolocationRequired());
        eventData.put(DatabaseEventFieldNames.startInstant.name(), new Timestamp(event.getStartInstant()));
        eventData.put(DatabaseEventFieldNames.endInstant.name(), new Timestamp(event.getEndInstant()));
        eventData.put(DatabaseEventFieldNames.registrationStartInstant.name(), new Timestamp(event.getRegistrationStartInstant()));
        eventData.put(DatabaseEventFieldNames.registrationEndInstant.name(), new Timestamp(event.getRegistrationEndInstant()));
        String encodedEventPoster = null;
        try {
            encodedEventPoster = BitmapConverter.BitmapToCompressedString(event.getEventPoster(), this.stringMaximumLength);
        } catch (Exception e) {
            // the image could not be compressed small enough for some reason, so we unfortunately have to upload nothing.
            // this really should not be possible though
            encodedEventPoster = null;
        }
        eventData.put(DatabaseEventFieldNames.eventPoster.name(), encodedEventPoster);
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
        eventData.put(DatabaseEventFieldNames.description.name(), event.getDescription());
        eventData.put(DatabaseEventFieldNames.geolocationRequired.name(), event.getGeolocationRequired());
        eventData.put(DatabaseEventFieldNames.startInstant.name(), new Timestamp(event.getStartInstant()));
        eventData.put(DatabaseEventFieldNames.endInstant.name(), new Timestamp(event.getEndInstant()));
        eventData.put(DatabaseEventFieldNames.registrationStartInstant.name(), new Timestamp(event.getRegistrationStartInstant()));
        eventData.put(DatabaseEventFieldNames.registrationEndInstant.name(), new Timestamp(event.getRegistrationEndInstant()));
        String encodedEventPoster = null;
        try {
            encodedEventPoster = BitmapConverter.BitmapToCompressedString(event.getEventPoster(), this.stringMaximumLength);
        } catch (Exception e) {
            // the image could not be compressed small enough for some reason, so we unfortunately have to upload nothing.
            // this really should not be possible though
            encodedEventPoster = null;
        }
        eventData.put(DatabaseEventFieldNames.eventPoster.name(), encodedEventPoster);
        eventData.put(DatabaseEventFieldNames.qrCode.name(), event.getQrCode().getText());
        eventData.put(DatabaseEventFieldNames.capacity.name(), event.getCapacity());
        DocumentReference eventRef = event.getEventReference();
        eventRef.update(eventData);
        for (EntrantStatus entrantStatus : event.getEntrantStatuses()) {
            this.updateEntrantStatus(entrantStatus);
        }
    }

    private Boolean deleteEventNoThread(Event event) {
        if (event == null || event.getEventReference() == null) {
            return false;
        }
        for (EntrantStatus entrantStatus : event.getEntrantStatuses()) {
            this.deleteEntrantStatusNoThread(entrantStatus);
        }
        DocumentReference eventRef = event.getEventReference();
        Task task = eventRef.delete();
        try {
            Tasks.await(task);
        } catch (ExecutionException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Deletes the Event (and everything about them, such as entrantStatuses, etc) in the database.
     * @param event
     * @return true if Event was deleted, false if Event could not be found or if it could not be deleted.
     */
    public Boolean deleteEvent(Event event) {
        if (event == null || event.getEventReference() == null) {
            return false;
        }
        Thread thread = new Thread(() -> {
            this.deleteEventNoThread(event);
        });
        thread.start();
        return true;
    }

    /**
     * Requests to get a Facility's Events from the database.
     * Once the Events have all been fetched, which is done asynchronously, they will be returned
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

    /**
     * Requests to get all Events from the database.
     * Once the Events have all been fetched, which is done asynchronously, they will be returned
     * via the onAllEventsFetchListener method.
     * @param onAllEventsFetchListener
     */
    public void getAllEvents(OnAllEventsFetchListener onAllEventsFetchListener) {
        Thread thread = new Thread(() -> {
            ArrayList<Event> events = fetchAllEvents();
            onAllEventsFetchListener.onAllEventsFetch(events);
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
            
            Object descriptionTemp = eventData.get(DatabaseEventFieldNames.description.name());
            String description = (String) descriptionTemp;

            Object geolocationRequiredTemp = eventData.get(DatabaseEventFieldNames.geolocationRequired.name());
            if (geolocationRequiredTemp == null) {
                throw new EventDoesNotExist("this event was missing the geolocationReqired field");
            }
            Boolean geolocationRequired = (Boolean) geolocationRequiredTemp;

            Object startInstantTemp = eventData.get(DatabaseEventFieldNames.startInstant.name());
            if (startInstantTemp == null) {
                throw new EventDoesNotExist("this event was missing the startInstant field");
            }
            Timestamp startInstantTimestamp = (Timestamp) startInstantTemp;
            Instant startInstant = startInstantTimestamp.toInstant();

            Object endInstantTemp = eventData.get(DatabaseEventFieldNames.endInstant.name());
            if (endInstantTemp == null) {
                throw new EventDoesNotExist("this event was missing the endInstant field");
            }
            Timestamp endInstantTimestamp = (Timestamp) endInstantTemp;
            Instant endInstant = endInstantTimestamp.toInstant();

            Object registrationStartInstantTemp = eventData.get(DatabaseEventFieldNames.registrationStartInstant.name());
            if (registrationStartInstantTemp == null) {
                throw new EventDoesNotExist("this event was missing the registrationStartInstant field");
            }
            Timestamp registrationStartInstantTimestamp = (Timestamp) registrationStartInstantTemp;
            Instant registrationStartInstant = registrationStartInstantTimestamp.toInstant();

            Object registrationEndInstantTemp = eventData.get(DatabaseEventFieldNames.registrationEndInstant.name());
            if (registrationEndInstantTemp == null) {
                throw new EventDoesNotExist("this event was missing the registrationEndInstant field");
            }
            Timestamp registrationEndInstantTimestamp = (Timestamp) registrationEndInstantTemp;
            Instant registrationEndInstant = registrationEndInstantTimestamp.toInstant();

            Object eventPosterTemp = eventData.get(DatabaseEventFieldNames.eventPoster.name());
            if (eventPosterTemp == null) {
                throw new EventDoesNotExist("this event was missing the eventPoster field");
            }
            String encodedEventPoster = (String) eventPosterTemp;
            Bitmap eventPoster = BitmapConverter.StringToBitmap(encodedEventPoster);

            Object qrCodeTemp = eventData.get(DatabaseEventFieldNames.qrCode.name());
            QRCode qrCode = new QRCode((String) qrCodeTemp);

            Object capacityTemp = eventData.get(DatabaseEventFieldNames.capacity.name());
            Long capacityLong = (Long) capacityTemp;
            Integer capacity;
            if (capacityLong == null) {
                capacity = null;
            }
            else {
                capacity = capacityLong.intValue();
            }

            try {
                events.add(new Event(name, description, startInstant, endInstant, registrationStartInstant, registrationEndInstant, eventPoster, capacity, qrCode, geolocationRequired, new EntrantPool(), eventRefs.get(eventRefs.size()-1)));
            }
            catch (Exception e) {
                continue;
            }
        }

        for (Event event : events) {
            this.getEntrantStatuses(event, this);
        }

        return events;
    }

    private ArrayList<Event> fetchAllEvents() {
        ArrayList<Facility> facilities = this.fetchAllFacilities();
        ArrayList<Event> allEvents = new ArrayList<Event>();

        ArrayList<Event> events = new ArrayList<Event>();
        for (Facility facility: facilities) {
            events = this.fetchEvents(facility); // FIXME convert this to facility.getEvents() once things car run on a single thread
            if (events != null && !events.isEmpty()) {
                allEvents.addAll(events);
            }
        }

        return allEvents;
    }

    @Override
    public void onEventsFetch(Facility facility, ArrayList<Event> events) {
        for (Event event : events) {
            facility.addEvent(event);
        }
    }

    /**
     * Gets an Event from the database.
     * Once the Event has been fetched, it will be returned
     * via the onSingleEventFetchListener method (onSingleEventFetch)
     * @param eventPath
     * @param onSingleEventFetchListener
     */

    public void getSingleEvent(String eventPath, OnSingleEventFetchListener onSingleEventFetchListener) {
        Thread thread = new Thread(() -> {
            Event event = fetchSingleEvent(eventPath);
            onSingleEventFetchListener.onSingleEventFetch(event);
        });
        thread.start();

    }

    private Event fetchSingleEvent(String eventPath) {
        // TODO take qr path instead and validate it?
        DocumentReference singleEventRef = db.document(eventPath);
        Task<DocumentSnapshot> task = singleEventRef.get();
        DocumentSnapshot documentSnapshot = null;
        Event event = null;
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
        HashMap<String, Object> singleEventData = (HashMap<String, Object>) documentSnapshot.getData();
        if (singleEventData == null) {
            return null;
        }
        Object nameTemp = singleEventData.get(DatabaseEventFieldNames.name.name());
        if (nameTemp == null) {
            return null;
        }
        String name = (String) nameTemp;

        Object descriptionTemp = singleEventData.get(DatabaseEventFieldNames.description.name());
        String description = (String) descriptionTemp;

        Object geolocationRequiredTemp = singleEventData.get(DatabaseEventFieldNames.geolocationRequired.name());
        if (geolocationRequiredTemp == null) {
            throw new EventDoesNotExist("this event was missing the geolocationReqired field");
        }
        Boolean geolocationRequired = (Boolean) geolocationRequiredTemp;

        Object startInstantTemp = singleEventData.get(DatabaseEventFieldNames.startInstant.name());
        if (startInstantTemp == null) {
            throw new EventDoesNotExist("this event was missing the startInstant field");
        }
        Timestamp startInstantTimestamp = (Timestamp) startInstantTemp;
        Instant startInstant = startInstantTimestamp.toInstant();

        Object endInstantTemp = singleEventData.get(DatabaseEventFieldNames.endInstant.name());
        if (endInstantTemp == null) {
            throw new EventDoesNotExist("this event was missing the endInstant field");
        }
        Timestamp endInstantTimestamp = (Timestamp) endInstantTemp;
        Instant endInstant = endInstantTimestamp.toInstant();

        Object registrationStartInstantTemp = singleEventData.get(DatabaseEventFieldNames.registrationStartInstant.name());
        if (registrationStartInstantTemp == null) {
            throw new EventDoesNotExist("this event was missing the registrationStartInstant field");
        }
        Timestamp registrationStartInstantTimestamp = (Timestamp) registrationStartInstantTemp;
        Instant registrationStartInstant = registrationStartInstantTimestamp.toInstant();

        Object registrationEndInstantTemp = singleEventData.get(DatabaseEventFieldNames.registrationEndInstant.name());
        if (registrationEndInstantTemp == null) {
            throw new EventDoesNotExist("this event was missing the registrationEndInstant field");
        }
        Timestamp registrationEndInstantTimestamp = (Timestamp) registrationEndInstantTemp;
        Instant registrationEndInstant = registrationEndInstantTimestamp.toInstant();

        Object eventPosterTemp = singleEventData.get(DatabaseEventFieldNames.eventPoster.name());
        if (eventPosterTemp == null) {
            return null;
        }
        String encodedEventPoster = (String) eventPosterTemp;
        Bitmap eventPoster = BitmapConverter.StringToBitmap(encodedEventPoster);

        Object qrCodeTemp = singleEventData.get(DatabaseEventFieldNames.qrCode.name());
        if (qrCodeTemp == null) {
            return null;
        }
        QRCode qrCode = new QRCode((String) qrCodeTemp);

        Object capacityTemp = singleEventData.get(DatabaseEventFieldNames.capacity.name());
        Integer capacity = (Integer) capacityTemp;

        try {
            event = new Event(name, description, startInstant, endInstant, registrationStartInstant, registrationEndInstant, eventPoster, capacity, qrCode, geolocationRequired, new EntrantPool(), singleEventRef);
        }
        catch (Exception e) {
            return null;
        }
        return event;
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

    private Boolean deleteEntrantStatusNoThread(EntrantStatus entrantStatus) {
        if (entrantStatus == null || entrantStatus.getEntrantStatusReference() == null) {
            return false;
        }
        DocumentReference entrantStatusRef = entrantStatus.getEntrantStatusReference();
        Task task = entrantStatusRef.delete();
        try {
            Tasks.await(task);
        } catch (ExecutionException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Deletes the EntrantStatus in the database.
     * @param entrantStatus
     * @return true if EntrantStatus was deleted, false if EntrantStatus could not be found or if it could not be deleted.
     */
    public Boolean deleteEntrantStatus(EntrantStatus entrantStatus) {
        if (entrantStatus == null || entrantStatus.getEntrantStatusReference() == null) {
            return false;
        }
        Thread thread = new Thread(() -> {
            this.deleteEntrantStatusNoThread(entrantStatus);
        });
        thread.start();
        return true;
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
            try {
                event.addEntrant(entrantStatus.getEntrant(), entrantStatus.getJoinedFrom(), entrantStatus.getStatus());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
        notificationData.put(DatabaseNotificationFieldNames.read.name(), false);
        DocumentReference notificationRef = this.db.collection(DatabaseCollectionNames.notifications.name()).document();
        notificationRef.set(notificationData);

        return true;
    }

    /**
     * Requests to get a User's unread notifications from the database.
     * Once the notifications have all been fetched, which is done asynchronously, they will be returned
     * via the onNotificationFetchListener method.
     * Notifications are automatically marked as read in the database as soon as they are fetched.
     * @param userID
     * @param onNotificationFetchListener
     */
    public void getUnreadNotifications(String userID, OnNotificationFetchListener onNotificationFetchListener) {
        Thread thread = new Thread(() -> {
            ArrayList<Notification> notifications = null;
            try {
                notifications = fetchUnreadNotifications(userID);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            onNotificationFetchListener.onNotificationFetch(notifications);
        });
        thread.start();
    }

    private ArrayList<Notification> fetchUnreadNotifications(String userID) throws Exception {
        CollectionReference notificationCol = this.db.collection(DatabaseCollectionNames.notifications.name());
        ArrayList<Notification> notifications = new ArrayList<>();

        Task<QuerySnapshot> task = notificationCol.get();
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
        if (documentSnapshots.isEmpty()) {
            return null;
        }
        HashMap<String, Object> notificationData;

        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
            notificationData = (HashMap<String, Object>) documentSnapshot.getData();
            if (notificationData == null) {
                continue;
            }

            Object readTemp = notificationData.get(DatabaseNotificationFieldNames.read.name());
            if (readTemp == null) {
                throw new Exception("Notification in database has no read status associated with it, fix underlying cause!");
            }
            Boolean read = (Boolean) readTemp;
            if (read) {
                continue; // skip this notification as it has already been read
            }

            Object userIdTemp = notificationData.get(DatabaseNotificationFieldNames.userID.name());
            if (userIdTemp == null) {
                throw new Exception("Notification in database had no userID associated with it, fix underlying cause!");
            }
            String fetchedUserID = (String) userIdTemp;
            if (!fetchedUserID.equals(userID)) {
                continue; // no need to fetch this notification as it isn't for this user
            }

            Object notificationTextTemp = notificationData.get(DatabaseNotificationFieldNames.notificationText.name());
            if (notificationTextTemp == null) {
                throw new Exception("Notification in database had no notificationText associated with it, fix underlying cause!");
            }
            String notificationText = (String) notificationTextTemp;

            Object instantPostedTemp = notificationData.get(DatabaseNotificationFieldNames.instantPosted.name());
            if (instantPostedTemp == null) {
                throw new Exception("Notification in database had no instantPosted associated with it, fix underlying cause!");
            }
            Timestamp instantPostedTimestamp = (Timestamp) instantPostedTemp;
            Instant instantPosted = instantPostedTimestamp.toInstant();

            notifications.add(new Notification(userID, notificationText, instantPosted));

            // mark notification as read in the database
            DocumentReference notificationRef = documentSnapshot.getReference();
            HashMap<String, Object> newData = new HashMap<>();
            newData.put(DatabaseNotificationFieldNames.read.name(), true);
            notificationRef.update(newData);
        }

        return notifications;
    }
}
