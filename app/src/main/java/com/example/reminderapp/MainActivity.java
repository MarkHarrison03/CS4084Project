package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDateTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("MainActivity", "Hello World");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        Button notifyButton = findViewById(R.id.NewReminder);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Hello World");
                Intent newRemind = new Intent(MainActivity.this, NewReminderActivity.class);
                startActivity(newRemind);

            }
        });
    }

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
                // replace ic_notification with your own icon
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
