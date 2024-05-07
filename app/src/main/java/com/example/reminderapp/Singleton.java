package com.example.reminderapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Singleton {
    private  String currentUserEmail;
    private String currentUserId;

    private static Singleton instance;

    private ArrayList<Reminder> currentUserReminders;
    private ArrayList<Location> currentUserLocationsForReminders;
    private Singleton() {
        this.currentUserEmail = null;
        currentUserReminders = new ArrayList<>();
        currentUserLocationsForReminders = new ArrayList<>();

    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    public void setCurrentUserId(String currentUserId){
        this.currentUserId = currentUserId;
    }
    public String getCurrentUserId() {
        return currentUserId;
    }
    public void setCurrentUserEmail(String currentUserEmail){
        this.currentUserEmail = currentUserEmail;
    }
    public String getUserEmail() {
        return currentUserEmail;
    }

    public void addReminderToArr(Reminder newReminder){
        currentUserReminders.add(newReminder);
    }
    public ArrayList<Reminder> getUserReminders() {
        return currentUserReminders;
    }

    public void addLocation(Location location){
        currentUserLocationsForReminders.add(location);
    }

    public ArrayList<Location> getCurrentUserLocationsForReminders() {
        return currentUserLocationsForReminders;
    }


}
