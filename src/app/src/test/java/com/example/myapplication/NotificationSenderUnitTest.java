package com.example.myapplication;

import static org.junit.Assert.fail;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mockito;

public class NotificationSenderUnitTest {
    FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);

    @Test
    public void sendNotificationTest() {
        User user = null;
        try {
            user = new User("name", "email@test.ca", db);
        }
        catch (Exception exception) {
            fail();
        }
        NotificationSender.sendNotification(user, "notification");
        // TODO test that notification is actually received by the user
    }
}
