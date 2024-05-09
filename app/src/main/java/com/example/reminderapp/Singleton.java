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
    private Location tempLocation;
    public Location getTempLocation() {
        return tempLocation;
    }

    private int currentPosNavbar;
    public void setTempLocation(Location tempLocation) {
        this.tempLocation = tempLocation;
    }

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
    public void setCurrentPosNavbar(int pos){ this.currentPosNavbar = pos;}
    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public String getUserEmail() {
        return currentUserEmail;
    }
    public int getCurrentPosNavbar(){ return currentPosNavbar;}


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
