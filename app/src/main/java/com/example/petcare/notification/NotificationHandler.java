package com.example.petcare.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.petcare.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationHandler {

    private final Context context;  // Use Context instead of AppCompatActivity
    private final String channelId = "MyNotificationChannel";
    private final int notificationId = 1;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public NotificationHandler(Context context) {
        this.context = context.getApplicationContext();  // Use application context to avoid memory leaks
    }

    public void initialize() {
        createNotificationChannel();
        requestNotificationPermission();
        getToken();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel Name";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Use ActivityResultLauncher to request permission for Android 13+ (API 33)
            // This requires an Activity or Context to register the launcher, which can be passed from the UI components
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                requestPermissionLauncher = activity.registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                            if (isGranted) {
                                Log.d("Notifications", "Permission granted");
                            } else {
                                Log.d("Notifications", "Permission denied");
                                // Optionally, explain to the user why notifications are unavailable
                            }
                        }
                );

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
    }

    private void getToken() {
        // Get the Firebase token for push notifications
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Log.d("FCM Token", "Token: " + token);
                // Send this token to your server if needed
            } else {
                Log.w("FCM Token", "Fetching FCM registration token failed", task.getException());
            }
        });
    }

    public void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.pet_app_logo) // Use a suitable icon from your resources
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(notificationId, builder.build());
        } else {
            Log.e("Notifications", "Notification permission not granted, cannot show notification");
            // Optionally request permission again if not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context instanceof AppCompatActivity) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
    }
}
