package com.example.reminderapp;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String firstName;
    private String lastName;
    private String userName;
    ArrayList<Reminder> currentReminders;

    public User(String firstName, String lastName, String userName, ArrayList<Reminder> currentReminders){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.currentReminders = currentReminders;
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
    public void addUserReminder(Reminder r){
        currentReminders.add(r);
    }
    public void removeReminder(Reminder r){
        currentReminders.remove(r);
        currentReminders.removeIf(Objects::isNull);
    }
}
