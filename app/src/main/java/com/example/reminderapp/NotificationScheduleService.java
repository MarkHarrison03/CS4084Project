package com.example.reminderapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationScheduleService extends Service {
    private Handler handler;
    private final long INTERVAL = 30000;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference remindersRef = database.getReference().child("reminders");

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        scheduleTask();
    }

    private void scheduleTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Code to check the database for reminders
                // and store them locally
                checkAndStoreReminders();


                // Schedule the task again after INTERVAL
                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL); // Initially start after INTERVAL
    }

    private void checkAndStoreReminders() {
        Log.d("Running", "running now!");


        Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reminder currentReminder = Utils.parseDataToReminder(snapshot.toString());
                    if (currentReminder.getDateInput().toLocalDate().toString().equals(LocalDate.now().toString())) {
                        boolean unique = true;
                        for (Reminder r : Singleton.getInstance().getUserReminders()) {
                            if ((r.getDescription().equals(currentReminder.getDescription())) && (r.getTitle().equals(currentReminder.getTitle())) && (r.getEmail().equals(currentReminder.getEmail())) && (r.getDateInput().toLocalDate().equals(currentReminder.getDateInput().toLocalDate()))) {
                                unique = false;
                            }
                        }

                        if (unique) {
                            Singleton.getInstance().addReminderToArr(currentReminder);
                        }
                    }
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(NotificationScheduleService.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
}