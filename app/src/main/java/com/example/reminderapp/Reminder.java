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

    private long id;





    public Reminder(String description, String title, LocalDateTime dateInput) {
        this.description = description;
        this.dateInput = dateInput;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = null;
        this.id =  (int) (Math.random() * 100000 )+ 1;

    }

    public Reminder(String description, String title, LocalDateTime startDate, Location location, LocalDateTime endDate) {
        this.description = description;
        this.dateInput = startDate;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = location;
        this.endDate = endDate;
        this.id =  (int) (Math.random() * 100000 )+ 1;

    }

    public Reminder(String description, String title, LocalDateTime dateInput, long id) {
        this.description = description;
        this.dateInput = dateInput;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = null;
        this.id =  id;

    }
    public Reminder(String description, String title, LocalDateTime startDate, Location location, LocalDateTime endDate, long id) {
        this.description = description;
        this.dateInput = startDate;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = location;
        this.endDate = endDate;
        this.id =  id;

    }
    public Reminder() {
        this.description = "no input";
        this.title = "no input";
        this.dateInput = LocalDateTime.now();
        String email = Singleton.getInstance().getUserEmail();
        this.id =  (int) (Math.random() * 100000 )+ 1;


    }


    public LocalDateTime getDateInput() {
        return dateInput;
    }

    public String getDescription() {
        return description;
    }
    public long getID(){return id;}

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
        if(endDate == null){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateInput.format(formatter);
        String s = "Title: " + title + "\n" + "Description: " + description + "\n" + "Email:" + email + "\n" + "Date and Time: " + formattedDateTime + "\n" + "ID: " + id + "\n" ;
        return s;
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = dateInput.format(formatter);
            String formattedEndDateTime= endDate.format(formatter);
            String s = "Title: " + title + "\n" + "Description: " + description + "\n" + "Email:" + email + "\nLocation: " + location.getNickname() + "\n" + "Start Date and Time: " + formattedDateTime + "\n" + "End Date and Time: " + formattedEndDateTime + "\nID: " + id + "\n";
            return s;
        }
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
