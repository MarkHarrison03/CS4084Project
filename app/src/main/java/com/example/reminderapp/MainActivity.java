package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent notificationSchedule = new Intent(this, NotificationScheduleService.class);
        startService(notificationSchedule);

        Intent notificationSending = new Intent(this, NotificationSendingService.class);
        startService(notificationSending);

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
        };

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }

        setContentView(R.layout.activity_user_reminder);
        databaseQuery();

        for(Reminder r : Singleton.getInstance().getUserReminders()){
            Log.d("TODAY", r.toString());
        }

        String apiKey = BuildConfig.MAPS_API_KEY;

        if (TextUtils.isEmpty(apiKey)) {
            Log.e("Places test", "No api key");
            finish();
            return;
        }

        Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey);

        Places.createClient(this);

    }

    private boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Permission granted: " + permissions[i]);
                } else {
                    Log.d("MainActivity", "Permission denied: " + permissions[i]);
                }
            }
        }
    }

    public void databaseQuery(){

        ArrayList<Reminder> reminderList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference remindersRef = database.getReference().child("reminders");

        Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    reminderList.add(Utils.parseDataToReminder(snapshot.toString()));
                }
                displayReminders(reminderList);
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MainActivity.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void displayReminders(ArrayList<Reminder> reminderList){
        ListView listView = findViewById(R.id.listView);
        // Initialize Adapter
        ArrayAdapter<Reminder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderList);
        listView.setAdapter(adapter);
    }

}

