package com.example.reminderapp;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String firstName;
    private String lastName;
    private String userName;
    private ArrayList<Reminder> currentReminders;
    private ArrayList<Location> userLocations;

    public User(String firstName, String lastName, String userName, ArrayList<Reminder> currentReminders, ArrayList<Location> userLocations) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.currentReminders = currentReminders;
        this.userLocations = userLocations;
    }

    public ArrayList<Reminder> getCurrentReminders() {
        return currentReminders;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public ArrayList<Location> getUserLocations() {
        return userLocations;
    }

    public void setCurrentReminders(ArrayList<Reminder> currentReminders) {
        this.currentReminders = currentReminders;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserLocations(ArrayList<Location> userLocations) {
        this.userLocations = userLocations;
    }

    public void addUserReminder(Reminder r) {
        currentReminders.add(r);
    }

    public void removeReminder(Reminder r) {
        currentReminders.remove(r);
        currentReminders.removeIf(Objects::isNull);
    }

    public void addUserLocation(Location l) {
        userLocations.add(l);
    }

    public void removeUserLocation(Location l) {
        userLocations.remove(l);
        userLocations.removeIf(Objects::isNull);
    }
}
