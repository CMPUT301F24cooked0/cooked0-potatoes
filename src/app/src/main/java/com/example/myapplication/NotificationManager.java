package com.example.myapplication;

final public class NotificationManager { // this is a static class
    private NotificationManager() {
        // private constructor to prevent instantiation
    }

    public static void sendNotification(User user, String notification) {
        sendNotification(user.getUniqueID(), notification);
    }

    public static void sendNotification(String user_unique_id, String notification) {
        new DatabaseManager().createNotification(user_unique_id, notification);
    }
}
