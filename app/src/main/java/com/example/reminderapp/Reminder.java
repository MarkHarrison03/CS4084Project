package com.example.reminderapp;

import java.time.*;

public class Reminder {
    private String description;
    private String title;
    private LocalDateTime dateInput;
    private boolean canSend;

    public Reminder(String description, String title, LocalDateTime dateInput, boolean canSend){
        this.description = description;
        this.dateInput = dateInput;
        this.canSend = canSend;
        this.title = title;
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

    public boolean isCanSend() {
        return canSend;
    }

    public void setCanSend(boolean canSend) {
        this.canSend = canSend;
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

    public void activateReminder(Reminder reminder) {
        reminder.setCanSend(reminder.dateInput == LocalDateTime.now());
    }
}
