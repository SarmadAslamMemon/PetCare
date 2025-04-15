package com.example.petcare.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.petcare.SmartPaw;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("FCM", "From: " + remoteMessage.getFrom());

        // The service needs a NotificationHandler instance to show notifications.
        // We'll get this through a Service Locator pattern for simplicity.
        NotificationHandler notificationHandler = ((SmartPaw) getApplication()).getNotificationHandler();
        if (notificationHandler == null) {
            Log.e("FCM", "NotificationHandler not initialized in Application class");
            return;
        }


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("FCM", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification().getTitle() != null) {
                notificationHandler.showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody() != null ? remoteMessage.getNotification().getBody() : "");
            }
        }


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("FCM", "Message data payload: " + remoteMessage.getData());
            //Handle data payload here, for example:
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            if (title != null && body != null) {
                notificationHandler.showNotification(title, body);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM", "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);  // Implement this method if needed.
    }
}
