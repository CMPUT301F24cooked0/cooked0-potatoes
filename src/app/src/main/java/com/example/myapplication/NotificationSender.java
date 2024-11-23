package com.example.myapplication;

final public class NotificationSender { // this is a static class
    private NotificationSender() {
        // private constructor to prevent instantiation
    }

    public static void sendNotification(User user, String notification) {
        sendNotification(user.getUniqueID(), notification);
    }

    public static void sendNotification(String user_unique_id, String notification) {
        new DatabaseManager().createNotification(user_unique_id, notification);
    }
}
