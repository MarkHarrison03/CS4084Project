package com.example.reminderapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationSendingService extends Service {
    private Handler handler;
    private final long INTERVAL = 2000;

    @Override
    public void onCreate() {
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
                for (Reminder reminder : userReminders) {
                    System.out.println(reminder);

                    if (activateReminder(reminder)) {
                        System.out.println("Activated!");
                        createNotification(reminder);
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

    private void createNotification(Reminder reminder) {
        System.out.println("hi!");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                // replace ic_notification with your own icon
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(reminder.getTitle())
                .setContentText(reminder.getDescription())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        System.out.println("hi!2");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        System.out.println("hi!3");
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
        System.out.println("hi!4");
    }

    private boolean activateReminder(Reminder reminder) {
        LocalDateTime now = LocalDateTime.now();
        return reminder.getDateInput().getHour() == now.getHour() &&
                reminder.getDateInput().getMinute() == now.getMinute();
    }

}