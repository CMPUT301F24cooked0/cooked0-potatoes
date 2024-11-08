package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/*
This class is responsible for creating an event object using user input. It sets information about
the event and also gets information about the event.
 */
public class Event {
    private String name;
    private Date date;
    private Integer capacity;
    private Bitmap eventPoster;
    private QRCode qrCode;
    private final EntrantPool entrantPool;

    /***
     * Base constructor to consolidate code used by other constructors
     * @param name
     * @param date
     * @param eventPoster
     * @throws Exception
     */
    public Event(String name, Date date, Bitmap eventPoster) throws Exception {
        this.setName(name);
        this.setDate(date);
        this.setEventPoster(eventPoster);
        this.qrCode = new QRCode(); // TODO auto-generate text for QR code?
        this.entrantPool = new EntrantPool();
        this.setCapacity(null);
    }

    /**
     * create an event with a capacity
     * @param capacity
    */
    public Event(String name, Date date, Bitmap eventPoster, Integer capacity) throws Exception {
        this(name, date, eventPoster);
        this.setCapacity(capacity);
    }

    /**
     * invalidate the current QR code for this event. This sets the QR code text to null and updates the database
     * which means that any future scans of the QR code will point to nothing, since it is no longer in the database
     */
    public void invalidateQRCode() {
        this.qrCode.setText(null);
        // TODO update database
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
        // TODO update database
    }

    /**
     * set this event's date, throws an exception if the date is null or in the past
     * @param date
     * @throws Exception
     */
    public void setDate(Date date) throws Exception {
        if (date == null) {
            throw new Exception("cannot set event date to null");
        }
        if (date.before(new Date())) {
            // if the date is in the past / before "now"
            throw new Exception("cannot set event date in the past");
        }
        this.date = date;
        // TODO update database
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
        // TODO update database
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
            throw new Exception("event poster resolution too small");
        }
        if (eventPoster.getWidth() > 8192 || eventPoster.getHeight() > 8192) {
            throw new Exception("event poster resolution too large"); // TODO auto-scale down instead of throwing
        }
        this.eventPoster = eventPoster;
        // TODO update database
    }

    /**
     * set this event's QR code, throws an exception if null
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
        // TODO update database
    }

    /**
     * add an entrant to this event
     * @param entrant
     * @param joinedFrom
     * @throws EntrantAlreadyInPool
     */
    public void addEntrant(User entrant, LatLng joinedFrom) throws EntrantAlreadyInPool {
        this.entrantPool.addEntrant(entrant, joinedFrom); // entrantPool does validation for us
    }

    /**
     * remove an entrant from this event
     * @param entrant
     */
    public void removeEntrant(User entrant) {
        this.entrantPool.removeEntrant(entrant); // entrantPool does validation for us
    }

    /**
     * get this event's name
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * get this event's date
     * @return
     */
    public Date getDate() {
        return this.date;
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
    public ArrayList<User> getEntrants() {
        return this.entrantPool.getEntrants();
    }

    /**
     * get a list of this event's entrantStatuses
     * @return
     */
    public ArrayList<EntrantStatus> getEntrantStatuses() {
        return this.entrantPool.getEntrantStatuses();
    }

}
