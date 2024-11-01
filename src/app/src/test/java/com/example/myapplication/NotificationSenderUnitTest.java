package com.example.myapplication;

import static org.junit.Assert.fail;

import org.junit.Test;

public class NotificationSenderUnitTest {
    @Test
    public void sendNotificationTest() {
        User user = null;
        try {
            user = new User("name", "email@test.ca");
        }
        catch (Exception exception) {
            fail();
        }
        NotificationSender.sendNotification(user, "notification");
        // TODO test that notification is actually received by the user
    }
}
