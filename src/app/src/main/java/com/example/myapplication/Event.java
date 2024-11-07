package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Event {
    private String name;
    private Date date;
    private Integer capacity;
    private Bitmap eventPoster;
    private QRCode qrCode;
    private final EntrantPool entrantPool;
    private FirebaseFirestore db;
    private Facility facility;
    private DocumentReference facilityRef;
    private DocumentReference eventRef;
    private final String eventId;
    private HashMap<String, Object> eventData;

    public Event(String name, Date date, Bitmap eventPoster, Facility facility) throws Exception {
        this.db = FirebaseFirestore.getInstance();
        this.facility = facility;
        this.facilityRef = facility.getFacilityRef();
        this.eventRef = facilityRef.collection("events").document();
        this.eventId = eventRef.getId();
        this.setName(name);
        this.setDate(date);
        this.setEventPoster(eventPoster);
        this.setQrCode(new QRCode()); // TODO auto-generate text for QR code?
        this.entrantPool = new EntrantPool(this);
        this.setCapacity(null);
    }

    public Event(String name, Date date, Bitmap eventPoster, Facility facility, Integer capacity) throws Exception {
        this(name, date, eventPoster, facility);
        this.setCapacity(capacity);
    }

    public void invalidateQRCode() {
        this.qrCode.setText(null);
        eventData.put("qrCode", this.qrCode.getText());
        this.eventRef.update(eventData); // update database after invalidating QR code
    }

    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("cannot set event name to null");
        }
        if (name.isEmpty()) {
            throw new Exception("cannot set event name to empty string");
        }
        this.name = name;
        eventData.put("name", this.name);
        this.eventRef.update(eventData); // update database after setting name
    }

    public void setDate(Date date) throws Exception {
        if (date == null) {
            throw new Exception("cannot set event date to null");
        }
        if (date.before(new Date())) {
            // if the date is in the past / before "now"
            throw new Exception("cannot set event date in the past");
        }
        this.date = date;
        eventData.put("date", this.date);
        this.eventRef.update(eventData); // update database after setting date
    }

    public void setCapacity(Integer capacity) throws Exception {
        if (capacity != null) {
            if (capacity <= 0) {
                throw new Exception("capacity cannot be 0 or negative");
            }
        }
        // capacity == null is ok,
        // it implies that there is no limit or capacity applicable for this event
        this.capacity = capacity;
        eventData.put("capacity", this.capacity);
        this.eventRef.update(eventData); // update database after setting capacity
    }

    public void setEventPoster(Bitmap eventPoster) throws Exception {
        if (eventPoster == null) {
            throw new Exception("event poster cannot be null");
        }
        if (eventPoster.getWidth() < 256 || eventPoster.getHeight() < 256) {
            throw new Exception("event poster resolution too small");
        }
        if (eventPoster.getWidth() > 8192 || eventPoster.getHeight() > 8192) {
            throw new Exception("event poster resolution too large"); // TODO auto-scale down instead of throwing
        }
        this.eventPoster = eventPoster;
        eventData.put("eventPoster", this.eventPoster);
        this.eventRef.update(eventData); // update database after setting event poster
    }

    public void setQrCode(QRCode qrCode) throws Exception {
        if (qrCode == null) {
            throw new Exception("QR code cannot be null");
        }
        // QRCode text can be null however,
        // that is the correct way to indicate that the event has no QRCode,
        // as calling methods will still work
        this.qrCode = qrCode;
        eventData.put("qrCode", this.qrCode.getText());
        this.eventRef.update(eventData); // update database after setting QR code
    }

    public void addEntrant(User entrant, LatLng joinedFrom) throws EntrantAlreadyInPool {
        this.entrantPool.addEntrant(entrant, joinedFrom); // entrantPool does validation for us
    }

    public void removeEntrant(User entrant) {
        this.entrantPool.removeEntrant(entrant); // entrantPool does validation for us
    }

    public String getName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public Bitmap getEventPoster() {
        return this.eventPoster;
    }

    public QRCode getQrCode() {
        return this.qrCode;
    }

    public ArrayList<User> getEntrants() {
        return this.entrantPool.getEntrants();
    }

    public ArrayList<EntrantStatus> getEntrantStatuses() {
        return this.entrantPool.getEntrantStatuses();
    }
    public String getEventId() {
        return this.eventId; // return event id in database
    }
    public DocumentReference getEventRef() {
        return this.eventRef; // return reference to event in database
    }
}
