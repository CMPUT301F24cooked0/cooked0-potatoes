package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.time.Instant;

public class NotificationUnitTest {
    @Test
    public void firstConstructorTest() throws Exception {
        // test that constructor doesn't cause errors
        Notification notification = new Notification("12345", "notification text");
    }

    @Test
    public void secondConstructorTest() throws Exception {
        // test that constructor doesn't cause errors
        Notification notification = new Notification("12345", "notification text", Instant.now());
    }

    @Test
    public void getInstantPostedTest() throws Exception {
        Notification notification = new Notification("12345", "notification text");
        Instant instantPosted = notification.getInstantPosted();
        assertNotNull(instantPosted);
    }

    @Test
    public void getUserIDTest() throws Exception {
        Notification notification = new Notification("12345", "notification text");
        String userID = notification.getUserID();
        assertNotNull(userID);
        assertEquals(userID, "12345");
    }

    @Test
    public void getNotificationTextTest() throws Exception {
        Notification notification = new Notification("12345", "notification text");
        String notificationText = notification.getNotificationText();
        assertNotNull(notificationText);
        assertEquals(notificationText, "notification text");
    }
}
