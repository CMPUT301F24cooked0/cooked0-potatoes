package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager { // static class
    private final FirebaseFirestore db;

    public DatabaseManager() {
        this.db = FirebaseFirestore.getInstance();
    }

    public DocumentReference createUser(User user) {
        if (user == null) {
            return null;
        }
        String userID = user.getUniqueID();
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("isAdmin", user.isAdmin());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("profilePicture", user.getProfilePicture());
        userData.put("receivesOrgAdmNotifications", user.getReceivesOrgAdmNotifications());
        DocumentReference userRef = this.db.collection("users").document(userID);
        userRef.set(userData);
        CollectionReference facilityCol = userRef.collection("facility");
        DocumentReference facilityRef = this.createFacility(user, user.getFacility());

        return userRef;
    }

    public void updateUser(User user) {
        if (user == null) {
            return;
        }
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("profilePicture", user.getProfilePicture());
        userData.put("receivesOrgAdmNotifications", user.getReceivesOrgAdmNotifications());
        DocumentReference userRef = user.getUserReference();
        userRef.update(userData);
        this.updateFacility(user.getFacility());
    }

    public User getUser(String userID) {
        DocumentReference userRef = this.db.collection("users").document(userID);
        final User[] user = new User[1];

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<String, Object> userData = (HashMap<String, Object>) documentSnapshot.getData();
                if (userData == null) {
                    throw new UserDoesNotExist("this user does not exist in the database");
                }

                // get user data from document

                Object isAdminTemp = userData.get("isAdmin");
                if (isAdminTemp == null) {
                    throw new UserDoesNotExist("this user was missing the isAdmin field");
                }
                boolean isAdmin = (boolean) isAdminTemp;

                Object nameTemp = userData.get("name");
                if (nameTemp == null) {
                    throw new UserDoesNotExist("this user was missing the name field");
                }
                String name = (String) nameTemp;

                Object emailTemp = userData.get("email");
                if (emailTemp == null) {
                    throw new UserDoesNotExist("this user was missing the email field");
                }
                String email = (String) emailTemp;

                Object phoneNumberTemp = userData.get("phoneNumber");
                if (phoneNumberTemp == null) {
                    throw new UserDoesNotExist("this user was missing the phoneNumber field");
                }
                Long phoneNumber = (Long) phoneNumberTemp;

                Object profilePictureTemp = userData.get("profilePicture");
                if (profilePictureTemp == null) {
                    throw new UserDoesNotExist("this user was missing the profilePicture field");
                }
                Bitmap profilePicture = (Bitmap) profilePictureTemp;

                Object notificationTemp = userData.get("receivesOrgAdmNotifications");
                if (notificationTemp == null) {
                    throw new UserDoesNotExist("this user was missing the receivesOrgAdmNotifications field");
                }
                boolean receivesOrgAdmNotifications = (boolean) notificationTemp;

                try {
                    user[0] = new User(name, email, phoneNumber, profilePicture, isAdmin, receivesOrgAdmNotifications, userRef, null);
                } catch (Exception e) {
                    user[0] = null;
                    throw new RuntimeException(e);
                }
            }
        });
        Facility facility = this.getFacility(user[0]); // get user's facility
        user[0].setFacility(facility);

        return user[0];
    }

    public DocumentReference createFacility(User user, Facility facility) {
        if (user == null || facility == null) {
            return null;
        }
        DocumentReference userRef = user.getUserReference();
        DocumentReference facilityRef = userRef.collection("facility").document();
        HashMap<String, Object> facilityData = new HashMap<>();
        facilityData.put("name", facility.getName());
        facilityData.put("location", facility.getLocation());
        facilityRef.set(facilityData);
        CollectionReference eventCol = facilityRef.collection("events");

        return facilityRef;
    }

    public void updateFacility(Facility facility) {
        if (facility == null) {
            return;
        }
        HashMap<String, Object> facilityData = new HashMap<>();
        facilityData.put("name", facility.getName());
        facilityData.put("location", facility.getLocation());
        DocumentReference facilityRef = facility.getFacilityReference();
        facilityRef.update(facilityData);
        for (Event event : facility.getEvents()) {
            this.updateEvent(event);
        }
    }

    public Facility getFacility(User organizer) {
        DocumentReference userRef = organizer.getUserReference();
        CollectionReference facilityCol = userRef.collection("facility");
        final Facility[] facility = new Facility[1];
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

                Object nameTemp = facilityData.get("name");
                if (nameTemp == null) {
                    throw new FacilityDoesNotExist("this facility was missing the name field");
                }
                String name = (String) nameTemp;

                Object locationTemp = facilityData.get("location");
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
        ArrayList<Event> events = this.getEvents(facility[0]); // get facility's events
        for (Event event : events) {
            facility[0].addEvent(event);
        }

        return facility[0];
    }

    public DocumentReference createEvent(Facility facility, Event event) {

    }

    public void updateEvent(Event event) {

    }

    public ArrayList<Event> getEvents(Facility facility) {

    }

    public DocumentReference createEntrantPool(Event event, EntrantPool entrantPool) {

    }

    public void updateEntrantPool(EntrantPool entrantPool) {

    }

    public EntrantPool getEntrantPool(Event event) {

    }

    public DocumentReference createEntrantStatus(EntrantPool entrantPool, EntrantStatus entrantStatus) {

    }

    public void updateEntrantStatus(EntrantStatus entrantStatus) {

    }

    public ArrayList<EntrantStatus> getEntrantStatuses(EntrantPool entrantPool) {

    }

    public DocumentReference createQrCode(Event event, QRCode qrCode) {

    }

    public void updateQrCode(QRCode qrCode) {

    }

    public QRCode getQrCode(Event event) {

    }
}
