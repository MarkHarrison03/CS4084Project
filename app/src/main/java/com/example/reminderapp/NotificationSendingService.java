package com.example.reminderapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class NotificationSendingService extends Service {
    private Handler handler;
    private final long INTERVAL = 2000;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        System.out.println("Oncreatestarted");
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        scheduleTask();
    }

    private void scheduleTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Parse reminders and send notification
                System.out.println("Started");
                ArrayList<Reminder> userReminders = Singleton.getInstance().getUserReminders();
                getLastLocation();
                for (Reminder reminder : userReminders) {
                    System.out.println(reminder);

                    if (activateReminder(reminder)) {
                        System.out.println("Activated!");
                        createNotification(reminder);
                        reminder.setIsSent(true);
                    }
                }
                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL); // Initially start after INTERVAL
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the handler when the service is destroyed
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // This service doesn't support binding
        return null;
    }
    private void createNotificationChannel() {

        CharSequence name = "My Notification Channel";
        String description = "Channel Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
        channel.setDescription(description);
        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotification(Reminder reminder) {
        createNotificationChannel();
        System.out.println("hi!");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")

                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(reminder.getTitle())
                .setContentText(reminder.getDescription())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        System.out.println("hi!2");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        System.out.println("hi!3");
        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
        System.out.println("hi!4");
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Singleton.getInstance().setCurrentLatitude(location.getLatitude());
                                Singleton.getInstance().setCurrentLongitude(location.getLongitude());
                            }
                        }
                    });
        }
    }

    private boolean radiusCheck(com.example.reminderapp.Location L) {
        float[] results = new float[1];
        double latitude = Singleton.getInstance().getCurrentLatitude();
        double longitude = Singleton.getInstance().getCurrentLongitude();
        Location.distanceBetween(L.getLatitude(), L.getLongitude(), latitude, longitude, results);
        float distanceInMeters = results[0];

        return distanceInMeters < L.getAccuracyRadius();
    }

    private boolean activateReminder(Reminder reminder) {
        if (reminder.getIsSent()) {
            return false;
        } else {
            if (reminder.getLocation() != null) {
                System.out.println("LOCATION!");
                boolean atLocation = radiusCheck(reminder.getLocation());
                LocalDateTime now = LocalDateTime.now();
                boolean time = reminder.getDateInput().getHour() == now.getHour() &&
                        reminder.getDateInput().getMinute() == now.getMinute();
                return (atLocation && time);
            } else {
                System.out.println("time based");
                LocalDateTime now = LocalDateTime.now();
                return reminder.getDateInput().getHour() == now.getHour() &&
                        reminder.getDateInput().getMinute() == now.getMinute();
            }
        }
    }
}