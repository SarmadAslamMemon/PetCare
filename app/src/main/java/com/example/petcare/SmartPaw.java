package com.example.petcare;

import android.app.Application;

import com.example.petcare.notification.NotificationHandler;

public class SmartPaw extends Application {
    private NotificationHandler notificationHandler;

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    public void setNotificationHandler(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize NotificationHandler
        notificationHandler = new NotificationHandler(this);
        notificationHandler.initialize();  // Initialize everything (notification channel, permissions, etc.)
    }
}
