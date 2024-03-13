package com.example.reminderapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.time.*;
import java.util.ArrayList;

public class Reminder {
    private String description;
    private String title;
    private LocalDateTime dateInput;
    private String email;


    public Reminder(String description, String title, LocalDateTime dateInput) {
        this.description = description;
        this.dateInput = dateInput;
        this.title = title;
        this.email = Singleton.getInstance().getUserEmail();
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


    public void setDateInput(int year, int month, int day, int hour, int minute) {
        this.dateInput = LocalDateTime.of(year, month, day, hour, minute);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean activateReminder(Reminder reminder) {
        return reminder.dateInput == LocalDateTime.now();
    }

    public void reminderParser(ArrayList<Reminder> reminders) {
        for (Reminder r : reminders) {
            activateReminder(r);
        }
    }

    public String toString(){
        String s = "Title: " + title + "\n" + "Description: " + description + "\n" + "Email:" + email + "\n" + "Date and Time: " + dateInput + "\n";
        return s;
    }
    //Method to get today's reminders and add them to a separate list to save on cpu and memory usage
    public void todayReminders(ArrayList<Reminder> reminders, User user) {
        LocalDate currentDate = LocalDate.now();
        reminders = user.getUserReminders();
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
