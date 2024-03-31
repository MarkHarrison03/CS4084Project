package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationPullRunnable {
    private Timer timer;
    private Handler handler;
    private final long INTERVAL = 2 * 60 * 100  ;

    public NotificationPullRunnable() {
        handler = new Handler(Looper.getMainLooper());
        timer = new Timer();
        scheduleTask();
    }

    private void scheduleTask() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Code to check the database for reminders
                        // and store them locally
                        checkAndStoreReminders();
                    }
                });
            }
        }, 0, INTERVAL);
    }

    private void checkAndStoreReminders() {
        Log.d("RUNNING", "running now!!");
    }

    public void stopScheduler() {
        timer.cancel();
    }
}