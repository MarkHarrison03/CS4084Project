package com.example.reminderapp;

import java.util.ArrayList;

public class Singleton {
    private  String currentUserEmail;
    private static Singleton instance;

    private ArrayList<Reminder> currentUserReminders;

    private Singleton() {

        this.currentUserEmail = null;
        currentUserReminders = new ArrayList<>();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
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
}