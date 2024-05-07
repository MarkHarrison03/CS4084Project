package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResultCallback;
import android.location.Address;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.text.TextUtils;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;





import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private PlacesClient placesClient;

    private boolean isFineLocationPermissionGranted = false;
    private boolean isCoarseLocationPermissionGranted = false;
    private boolean isNotificationPermissionGranted = false;
    private ActivityResultLauncher<String[]> mPermissionResultLauncher;
    FusedLocationProviderClient fusedLocationProviderClient;
    private double lastKnownLatitude;
    private double lastKnownLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent serviceIntent = new Intent(this, NotificationScheduleService.class);
        startService(serviceIntent);
        Intent serviceIntent1 = new Intent(this, NotificationSendingService.class);
        startService(serviceIntent1);

        Log.d("MainActivity", "Hello World");

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        //createNotificationChannel();
        Button newRemindButton = findViewById(R.id.NewReminder);
        Button myRemindersButton = findViewById(R.id.MyReminders);
        Button mapButton = findViewById(R.id.map);
        ImageButton profileButton = findViewById(R.id.profileButton);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        getLastLocation();

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });



        newRemindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "New Reminder Button clicked");
                Intent newRemind = new Intent(MainActivity.this, NewReminderActivity.class);
                startActivity(newRemind);
            }
        });

        myRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("MainActivity", "View Reminder Button clicked");

                Intent myReminders = new Intent(MainActivity.this, UserReminders.class);
                startActivity(myReminders);

            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mapview) {
                Intent newMap = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(newMap);
            }
        });

        String apiKey = BuildConfig.MAPS_API_KEY;

        // Log an error if apiKey is not set.
        if (TextUtils.isEmpty(apiKey)) {
            Log.e("Places test", "No api key");
            finish();
            return;
        }

        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        placesClient = Places.createClient(this);









    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPermissions();
    }





    private void requestPermissions() {
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {
                    isFineLocationPermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                if (result.get(Manifest.permission.ACCESS_COARSE_LOCATION) != null) {
                    isCoarseLocationPermissionGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                }

                if (result.get(Manifest.permission.POST_NOTIFICATIONS) != null) {
                    isNotificationPermissionGranted = result.get(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        });

        List<String> permissionRequest = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS);
        }

        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                lastKnownLatitude = location.getLatitude();
                                lastKnownLongitude = location.getLongitude();
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (!addresses.isEmpty()) {
                                        Address address = addresses.get(0);
                                        Log.d("MainActivity", "Last Known Location: " + address.getLocality() + ", " + address.getCountryName());
                                        Log.d("MainActivity", "Last Known Location: Latitude=" + lastKnownLatitude + ", Longitude=" + lastKnownLongitude);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }}}


    private void createNotificationChannel() {

        CharSequence name = "My Notification Channel";
        String description = "Channel Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
        channel.setDescription(description);
        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    //creates reminder based on reminder object details
    private void createNotification(Reminder reminder) {
        System.out.println("hi!");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")

                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(reminder.getTitle() + reminder.getDateInput())
                .setContentText(reminder.getDescription())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        System.out.println("hi!2");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        System.out.println("hi!3");
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
        System.out.println("hi!4");

    }


}

