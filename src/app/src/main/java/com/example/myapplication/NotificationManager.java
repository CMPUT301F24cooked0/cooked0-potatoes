package com.example.myapplication;

final public class NotificationManager { // this is a static class
    private NotificationManager() {
        // private constructor to prevent instantiation
    }

    /**
     * The "easy" sendNotification method, just provide a User and some String for the notification
     * The instantPosted will be set to **now**
     * @param user
     * @param notificationText
     * @throws Exception
     */
    public static void sendNotification(User user, String notificationText) throws Exception {
        Notification notification = new Notification(user.getUniqueID(), notificationText);
        sendNotification(notification);
    }

    /**
     * If you wanted to create the Notification object yourself, this is the sendNotification method
     * to use. However if you just had a User and some String to send "now", consider using the
     * other sendNotification method as it may be simpler.
     * @param notification
     */
    public static void sendNotification(Notification notification) {
        new DatabaseManager().createNotification(notification);
    }
}
