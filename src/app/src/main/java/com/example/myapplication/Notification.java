package com.example.myapplication;

import java.time.Instant;

public class Notification {
    private String userID;
    private String notificationText;
    private Instant instantPosted;

    /**
     * Simplest Notification constructor, sets the instantPosted to **now**
     * @param userID
     * @param notificationText
     * @throws Exception
     */
    public Notification(String userID, String notificationText) throws Exception {
        this.setUserID(userID);
        this.setNotificationText(notificationText);
        this.instantPosted = Instant.now();
    }

    /**
     * Notification constructor where you specify the instantPosted.
     * If you are creating a new notification, you can use the other constructor as it will
     * initialize the instantPosted to **now** for you.
     * @param userID
     * @param notificationText
     * @param instantPosted
     * @throws Exception
     */
    public Notification(String userID, String notificationText, Instant instantPosted) throws Exception {
        this(userID, notificationText);
        this.setInstantPosted(instantPosted);
    }

    private void setInstantPosted(Instant instantPosted) throws Exception {
        if (instantPosted == null) {
            throw new Exception("Notification instantPosted cannot be null");
        }
        if (instantPosted.isAfter(Instant.now())) {
            throw new Exception("Notification instantPosted cannot be in the future");
        }
        this.instantPosted = instantPosted;
    }

    private void setNotificationText(String notificationText) throws Exception {
        if (notificationText == null) {
            throw new Exception("Notification text cannot be null");
        }
        if (notificationText.isEmpty()) {
            throw new Exception("Notification text cannot be empty");
        }
        this.notificationText = notificationText;
    }

    private void setUserID(String userID) throws Exception {
        if (userID == null) {
            throw new Exception("Notification userID cannot be null");
        }
        this.userID = userID;
    }

    /**
     * gets the Instant that the notification was posted
     * @return
     */
    public Instant getInstantPosted() {
        return this.instantPosted;
    }

    /**
     * gets the notification text
     * @return
     */
    public String getNotificationText() {
        return this.notificationText;
    }

    /**
     * gets the user ID for the User to which the notification is being sent to
     * @return
     */
    public String getUserID() {
        return this.userID;
    }
}
