package com.example.reminderapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Singleton {
    private String currentUserEmail;
    private String currentUserId;

    private static Singleton instance;
    private double currentLatitude;
    private double currentLongitude;

    private ArrayList<Reminder> currentUserReminders;

    private Singleton() {
        this.currentUserEmail = null;
        currentUserReminders = new ArrayList<>();
        currentLatitude = 0.0f;
        currentLongitude = 0.0f;
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public String getUserEmail() {
        return currentUserEmail;
    }

    public void addReminderToArr(Reminder newReminder) {
        currentUserReminders.add(newReminder);
    }

    public ArrayList<Reminder> getUserReminders() {
        return currentUserReminders;
    }

    public void setCurrentLatitude(double latitude) {
        currentLatitude = latitude;
    }

    public void setCurrentLongitude(double longitude) {
        currentLongitude = longitude;
    }

    public double getCurrentLatitude(){
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }
}
