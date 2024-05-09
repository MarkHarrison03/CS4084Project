package com.example.reminderapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.lang.reflect.Array;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Reminder {
    private String description;
    private String title;
    private LocalDateTime dateInput;
    private String email;
    private Location location;
    private boolean isSent = false;
    private LocalDateTime endDate;


    public Reminder(String description, String title, LocalDateTime dateInput) {
        this.description = description;
        this.dateInput = dateInput;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = null;

    }

    public Reminder(String description, String title, LocalDateTime startDate, Location location, LocalDateTime endDate) {
        this.description = description;
        this.dateInput = startDate;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = location;
        this.endDate = endDate;
    }
    public Reminder() {
        this.description = "no input";
        this.title = "no input";
        this.dateInput = LocalDateTime.now();
        String email = Singleton.getInstance().getUserEmail();
    }

    public LocalDateTime getDateInput() {
        return dateInput;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public Location getLocation() {
        return location;
    }


    public void setDateInput(int year, int month, int day, int hour, int minute) {
        this.dateInput = LocalDateTime.of(year, month, day, hour, minute);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsSent(boolean isSent){
        this.isSent = isSent;
    }

    public boolean getIsSent(){
        return isSent;
    }

    public String toString() {
        String s = "Title: " + title + "\n" + "Description: " + description + "\n" + "Email:" + email + "\n" + "Date and Time: " + dateInput + "\n" + "location: " + location.toString() + "\n" ;
        return s;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
