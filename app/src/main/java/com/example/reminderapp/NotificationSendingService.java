package com.example.reminderapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.okhttp.internal.Util;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationSendingService extends Service {
    private Handler handler;
    private final long INTERVAL = 2000;

    FusedLocationProviderClient fusedLocationProviderClient;

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private LocationRequest locationRequest;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {

                System.out.println("LOCATION UPDATE ");
                Singleton.getInstance().setCurrentLatitude(location.getLatitude());
                Singleton.getInstance().setCurrentLongitude(location.getLongitude());
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(INTERVAL);
        requestLocationUpdates();
        handler = new Handler(Looper.getMainLooper());
        scheduleTask();
    }

    private void sendBroadcastToMainActivity() {
        Intent intent = new Intent("refresh");
        sendBroadcast(intent);
    }

    private void scheduleTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Schedulesrvc", "Notification srvc started");
                getLastLocation();
                Log.d("ScheduleSrvc", "LOC LAT:" + Singleton.getInstance().getCurrentLatitude());
                Log.d("ScheduleSrvc", "LOC LONG:" + Singleton.getInstance().getCurrentLongitude());
                ArrayList<Reminder> userReminders = Singleton.getInstance().getUserReminders();
                for (Reminder reminder : userReminders) {

                    if (activateReminder(reminder)) {
                        System.out.println("Activated!");
                        createNotification(reminder);
                        reminder.setIsSent(true);
                        Utils.deleteFromDB(reminder.getID());
                        //sendBroadcastToMainActivity();
                    }
                }
                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Schedulesrvc", "Notification srvc ended");

        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {

        CharSequence name = "My Notification Channel";
        String description = "Channel Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotification(Reminder reminder) {
        createNotificationChannel();
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")

                .setSmallIcon(R.drawable.geo_echo_icons_03)
                .setLargeIcon(largeIconBitmap)
                .setContentTitle(reminder.getTitle())
                .setContentText(reminder.getDescription())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());

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
        System.out.println("system lat" + latitude);
        double longitude = Singleton.getInstance().getCurrentLongitude();
        System.out.println("system longt" + longitude);

        System.out.println("Reminder lat long =  " + L.getLatitude()  +" " + L.getLongitude());
        Location.distanceBetween(L.getLatitude(), L.getLongitude(), latitude, longitude, results);
        float distanceInMeters = results[0];
        System.out.println("distance " + distanceInMeters);
        System.out.println(L.getAccuracyRadius());
        return distanceInMeters < L.getAccuracyRadius();
    }

    private boolean activateReminder(Reminder reminder) {
        if (reminder.getIsSent()) {
            return false;
        } else {
            if (reminder.getLocation() != null) {
                boolean atLocation = radiusCheck(reminder.getLocation());
                System.out.println(atLocation);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startDate = reminder.getDateInput();
                LocalDateTime endDate = reminder.getEndDate();
                System.out.println("Enddate" + endDate);
                System.out.println();
                boolean time = now.isAfter(startDate) || now.isEqual(startDate) && now.isBefore(endDate);
                return (atLocation && time);
            } else {
                LocalDateTime now = LocalDateTime.now();
                return reminder.getDateInput().getHour() == now.getHour() &&
                        reminder.getDateInput().getMinute() == now.getMinute();
            }
        }
    }
}