package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.time.*;

/*
This class is responsible for creating an event object using user input. It sets information about
the event and also gets information about the event.
 */
public class Event {
    private String name;
    private String description;
    private Instant startInstant;
    private Instant endInstant;
    private Instant registrationStartInstant;
    private Instant registrationEndInstant;
    private Integer capacity;
    private Bitmap eventPoster;
    private QRCode qrCode;
    private Boolean geolocationRequired;
    private final EntrantPool entrantPool;
    private DocumentReference eventRef;
    private String details; // Add this field

    /***
     * Base constructor to consolidate code used by other constructors.
     * Note that this sets QRCode's text to null, but it should be set as soon as it is known
     * @param name
     * @param startInstant
     * @param endInstant
     * @param registrationStartInstant
     * @param registrationEndInstant
     * @param eventPoster
     * @param geolocationRequired
     */
    public Event(String name, Instant startInstant, Instant endInstant, Instant registrationStartInstant, Instant registrationEndInstant, Bitmap eventPoster, Boolean geolocationRequired) throws Exception {
        this.setName(name);
        this.setGeolocationRequired(geolocationRequired);
        this.setStartInstant(startInstant);
        this.setEndInstant(endInstant);
        this.setRegistrationStartInstant(registrationStartInstant);
        this.setRegistrationEndInstant(registrationEndInstant);
        this.setEventPoster(eventPoster);
        this.qrCode = new QRCode();
        this.setQrCode(qrCode);
        this.entrantPool = new EntrantPool();
        this.setCapacity(null);
    }

    /**
     * create an event with a capacity.
     * Note that this sets QRCode's text to null, but it should be set as soon as it is known
     * @param name
     * @param startInstant
     * @param endInstant
     * @param registrationStartInstant
     * @param registrationEndInstant
     * @param eventPoster
     * @param capacity
     * @param geolocationRequired
     */
    public Event(String name, Instant startInstant, Instant endInstant, Instant registrationStartInstant, Instant registrationEndInstant, Bitmap eventPoster, Integer capacity, Boolean geolocationRequired) throws Exception {
        this(name, startInstant, endInstant, registrationStartInstant, registrationEndInstant, eventPoster, geolocationRequired);
        this.setCapacity(capacity);
    }

    /**
     * create an event with a description.
     * Note that this sets QRCode's text to null, but it should be set as soon as it is known
     * @param name
     * @param description
     * @param startInstant
     * @param endInstant
     * @param registrationStartInstant
     * @param registrationEndInstant
     * @param eventPoster
     * @param geolocationRequired
     */
    public Event(String name, String description, Instant startInstant, Instant endInstant, Instant registrationStartInstant, Instant registrationEndInstant, Bitmap eventPoster, Boolean geolocationRequired) throws Exception {
        this(name, startInstant, endInstant, registrationStartInstant, registrationEndInstant, eventPoster, geolocationRequired);
        this.setDescription(description);
    }

    /**
     * only use this constructor in DatabaseManager to instantiate an Event from the data in the database
     * @param name
     * @param description
     * @param startInstant
     * @param endInstant
     * @param registrationStartInstant
     * @param registrationEndInstant
     * @param eventPoster
     * @param capacity
     * @param qrCode
     * @param geolocationRequired
     * @param entrantPool
     * @param eventRef
     */
    public Event(String name, String description, Instant startInstant, Instant endInstant, Instant registrationStartInstant, Instant registrationEndInstant, Bitmap eventPoster, Integer capacity, QRCode qrCode, Boolean geolocationRequired, EntrantPool entrantPool, DocumentReference eventRef) throws Exception {
        this.setName(name);
        this.setDescription(description);
        this.setGeolocationRequired(geolocationRequired);
        this.setStartInstant(startInstant);
        this.setEndInstant(endInstant);
        this.setRegistrationStartInstant(registrationStartInstant);
        this.setRegistrationEndInstant(registrationEndInstant);
        this.setEventPoster(eventPoster);
        this.setCapacity(capacity);
        this.setQrCode(qrCode);
        this.entrantPool = entrantPool;
        this.eventRef = eventRef;
    }

    /**
     * invalidate the current QR code for this event. This sets the QR code text to null.
     * This Event needs to be updated in the database after this
     */

    public void invalidateQRCode() {
        this.qrCode.setText(null);
    }

    /**
     * set this event's name, throws an exception if the name is null or empty
     * @param name
     * @throws Exception
     */
    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("cannot set event name to null");
        }
        if (name.isEmpty()) {
            throw new Exception("cannot set event name to empty string");
        }
        this.name = name;
    }

    /**
     * set this event's description. can be null
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * set whether this Event requires geolocation or not. cannot be null
     * @param geolocationRequired
     */
    public void setGeolocationRequired(Boolean geolocationRequired) {
        if (geolocationRequired == null) {
            throw new RuntimeException("geolocationRequired cannot be null");
        }
        this.geolocationRequired = geolocationRequired;
    }

    /**
     * set this event's start instant, throws an exception if the instant is null or after end instant
     * @param startInstant
     * @throws Exception
     */
    public void setStartInstant(Instant startInstant) throws Exception {
        if (startInstant == null) {
            throw new Exception("cannot set event startInstant to null");
        }
        if (this.endInstant != null && this.endInstant.isBefore(startInstant)) {
            throw new Exception("cannot set event startInstant to after endInstant");
        }
        this.startInstant = startInstant;
    }

    /**
     * set this event's end instant, throws an exception if the instant is null or before start instant
     * @param endInstant
     * @throws Exception
     */
    public void setEndInstant(Instant endInstant) throws Exception {
        if (endInstant == null) {
            throw new Exception("cannot set event endInstant to null");
        }
        if (this.startInstant != null && endInstant.isBefore(this.startInstant)) {
            throw new Exception("cannot set event endInstant to before startInstant");
        }
        this.endInstant = endInstant;
    }

    public void setRegistrationStartInstant(Instant registrationStartInstant) throws Exception {
        if (registrationStartInstant == null) {
            throw new Exception("cannot set event registrationStartInstant to null");
        }
        if (this.registrationEndInstant != null && this.registrationEndInstant.isBefore(registrationStartInstant)) {
            throw new Exception("cannot set event registrationStartInstant to after registrationEndInstant");
        }
        this.registrationStartInstant = registrationStartInstant;
    }

    public void setRegistrationEndInstant(Instant registrationEndInstant) throws Exception {
        if (registrationEndInstant == null) {
            throw new Exception("cannot set event registrationEndInstant to null");
        }
        if (this.registrationStartInstant != null && registrationEndInstant.isBefore(this.registrationStartInstant)) {
            throw new Exception("cannot set event registrationEndInstant to before registrationStartInstant");
        }
        this.registrationEndInstant = registrationEndInstant;
    }

    /**
     * set this event's capacity, throws an exception if the capacity is 0 or negative
     * A capacity of null is valid, and means that the event has no capacity
     * @param capacity
     * @throws Exception
     */
    public void setCapacity(Integer capacity) throws Exception {
        if (capacity != null) {
            if (capacity <= 0) {
                throw new Exception("capacity cannot be 0 or negative");
            }
        }
        // capacity == null is ok,
        // it implies that there is no limit or capacity applicable for this event
        this.capacity = capacity;
    }

    /**
     * set this event's poster, throws an exception if the poster is null, or if image dimensions are too small or too large
     * @param eventPoster
     * @throws Exception
     */
    public void setEventPoster(Bitmap eventPoster) throws Exception {
        if (eventPoster == null) {
            throw new Exception("event poster cannot be null");
        }
        if (eventPoster.getWidth() < 256 || eventPoster.getHeight() < 256) {
            throw new Exception("event poster resolution too small (must be at least 256x256)");
        }
        int scalingFactor = 1;
        int width = eventPoster.getWidth();
        int height = eventPoster.getHeight();
        while (width/scalingFactor > 8192 || height/scalingFactor > 8192) {
            scalingFactor += 1;
        }
        if (scalingFactor != 1) {
            eventPoster = BitmapConverter.ScaledDownBitmap(eventPoster, scalingFactor); // scale down until it is small enough
        }
        this.eventPoster = eventPoster;
    }

    /**
     * set this event's QR code, throws an exception if null.
     * If this Event has no QR code, set QRCode text to null, but still provide a QRCode object
     * @param qrCode
     * @throws Exception
     */
    public void setQrCode(QRCode qrCode) throws Exception {
        if (qrCode == null) {
            throw new Exception("QR code cannot be null");
        }
        // QRCode text can be null however,
        // that is the correct way to indicate that the event has no QRCode,
        // as calling methods will still work
        this.qrCode = qrCode;
    }

    /**
     * add an entrant to this event
     * @param entrant
     * @param joinedFrom
     * @throws EntrantAlreadyInPool
     */
    public void addEntrant(User entrant, LatLng joinedFrom) throws Exception {
        if (Instant.now().isAfter(this.registrationEndInstant)) {
            throw new Exception("cannot register user, registration has ended");
        }
        if (Instant.now().isBefore(this.registrationStartInstant)) {
            throw new Exception("cannot register user, registration has not begun");
        }
        this.entrantPool.addEntrant(entrant, joinedFrom); // entrantPool does validation for us
    }

    /**
     * Add an entrant to this event with a custom initial status
     * @param entrant
     * @param joinedFrom
     * @param status
     * @throws EntrantAlreadyInPool
     */
    public void addEntrant(User entrant, LatLng joinedFrom, Status status) throws Exception {
        if (Instant.now().isAfter(this.registrationEndInstant)) {
            throw new Exception("cannot register user, registration has ended");
        }
        if (Instant.now().isBefore(this.registrationStartInstant)) {
            throw new Exception("cannot register user, registration has not begun");
        }
        this.entrantPool.addEntrant(entrant, joinedFrom, status);
    }

    /**
     * remove an entrant from this event
     * @param entrant
     */
    public void removeEntrant(User entrant) throws Exception {
        this.entrantPool.removeEntrant(entrant); // entrantPool does validation for us
    }

    /**
     * set an entrant's status
     * @param entrant
     * @param status
     */
    public void setEntrantStatus(User entrant, Status status) throws Exception {
        this.entrantPool.setEntrantStatus(entrant, status);
    }

    /**
     * get this event's name
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * get this event's description
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * get whether this Event requires geolocation or not
     * @return
     */
    public Boolean getGeolocationRequired() {
        return this.geolocationRequired;
    }

    /**
     * get this event's start instant
     * @return
     */
    public Instant getStartInstant() {
        return this.startInstant;
    }

    /**
     * get this event's end instant
     * @return
     */
    public Instant getEndInstant() {
        return this.endInstant;
    }

    /**
     * get this event's registration start instant
     * @return
     */
    public Instant getRegistrationStartInstant() {
        return this.registrationStartInstant;
    }

    /**
     * get this event's registration end instant
     * @return
     */
    public Instant getRegistrationEndInstant() {
        return this.registrationEndInstant;
    }

    /**
     * get this event's capacity
     * @return
     */
    public Integer getCapacity() {
        return this.capacity;
    }

    /**
     * get this event's poster
     * @return
     */
    public Bitmap getEventPoster() {
        return this.eventPoster;
    }

    /**
     * get this event's QR code
     * @return
     */
    public QRCode getQrCode() {
        return this.qrCode;
    }

    /**
     * get a list of this event's entrants
     * @return
     */
    public ArrayList<User> getEntrants() throws Exception {
        return this.entrantPool.getEntrants();
    }

    public ArrayList<EntrantStatus> getEntrantStatuses() {
        return this.entrantPool.getEntrantStatuses();
    }

    public void setEventReference(DocumentReference eventRef) {
        this.eventRef = eventRef;
    }

    /**
     * get aDocumentReference to this event in the database
     * @return
     */
    public DocumentReference getEventReference() {
        return this.eventRef; // return reference to event in database
    }
}