package com.example.reminderapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class LocationReminder extends Reminder {
    Location location;

    public LocationReminder(String description, String title, LocalDateTime dateInput, Location location) {
        super(description, title, dateInput);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    //need to add method for checking location against system location and setting can send to true
    public void activateLocationReminder(LocationReminder locationReminder) {
        //CODE TO VALIDATE LOCATION WILL GO HERE
    }

    public void locationReminderParser(ArrayList<LocationReminder> locationReminders, User user) {
        locationReminders = user.getUserLocationReminders();
        for (LocationReminder locRem : locationReminders) {
            activateLocationReminder(locRem);
        }
    }
}
