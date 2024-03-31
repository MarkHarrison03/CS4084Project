package com.example.reminderapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.reminderapp.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NotificationScheduleService extends Service {
    private Handler handler;
    private final long INTERVAL = 10000;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference remindersRef = database.getReference().child("reminders");

    @Override
    public void onCreate() {
        System.out.println("service activated");
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
                    System.out.println("okaythismethodhasbeencalled");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reminder currentReminder = Utils.parseDataToReminder(snapshot.toString());
                    System.out.println("-----------------------");
                    System.out.println("Selected reminder date:" + currentReminder.getDateInput().toLocalDate());
                    System.out.println("Current date:" + LocalDate.now());
                    System.out.println("ARE THEY THE SAME: " + LocalDate.now() == currentReminder.getDescription());

                    if(currentReminder.getDateInput().toLocalDate().toString().equals(LocalDate.now().toString())){
                        boolean unique = true;
                        for(Reminder r : Singleton.getInstance().getUserReminders()){
                            System.out.println(currentReminder.getTitle());
                            System.out.println(r.getTitle());
                            System.out.println("next 4 booleans are desc, title, email and datetime");
                            System.out.println(r.getDescription().equals(currentReminder.getDescription()));
                            System.out.println(r.getTitle().equals(currentReminder.getTitle()));
                            System.out.println(r.getEmail().equals(currentReminder.getEmail()));
                            System.out.println(r.getDateInput().toLocalDate().isEqual(currentReminder.getDateInput().toLocalDate()));
                            if ((r.getDescription().equals(currentReminder.getDescription())) && (r.getTitle().equals(currentReminder.getTitle()) ) && (r.getEmail().equals(currentReminder.getEmail())) && (r.getDateInput().toLocalDate().equals(currentReminder.getDateInput().toLocalDate()))) {
                                System.out.println(r.getTitle() + "is already in the reminders");
                                unique = false;
                            }
                        }

                        if(unique) {
                            Singleton.getInstance().addReminderToArr(currentReminder);
                            System.out.println("That evaluated to true");
                            System.out.println(Singleton.getInstance().getUserReminders().contains(currentReminder));
                            System.out.println(Singleton.getInstance().getUserReminders().contains(currentReminder));
                        }
                        System.out.println("ADDED");
                        System.out.println("-----------------------");

                    }
                }
                System.out.println("STARTING DB CHECK");
                for(Reminder r: Singleton.getInstance().getUserReminders()) {
                    System.out.println(Singleton.getInstance().getUserReminders().size());
                    System.out.println(r.getDescription());
                }
                System.out.println("FINISHED DB CHECK");
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