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


    public Reminder(String description, String title, LocalDateTime dateInput) {
        this.description = description;
        this.dateInput = dateInput;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = null;

    }

    public Reminder(String description, String title, LocalDateTime dateInput, Location location) {
        this.description = description;
        this.dateInput = dateInput;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
        this.location = location;
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
        String s = "Title: " + title + "\n" + "Description: " + description + "\n" + "Email:" + email + "\n" + "Date and Time: " + dateInput + "\n";
        return s;
    }

    //Method to get today's reminders and add them to a separate list to save on cpu and memory usage
    public void todayReminders(User user) {
        LocalDate currentDate = LocalDate.now();
        ArrayList<Reminder> reminders = user.getUserReminders();
        for (Reminder r : reminders) {
            if (r.getDateInput().toLocalDate().equals(currentDate)) {
                user.addTodayReminder(r);
            }
        }
        //Checking if there are any reminders not for today's date in here and removing them
        for (Reminder rem : user.getTodayReminders()) {
            if (!(rem.getDateInput().toLocalDate().equals(currentDate))) {
                user.removeTodayReminder(rem);
            }


        }
    }

}
