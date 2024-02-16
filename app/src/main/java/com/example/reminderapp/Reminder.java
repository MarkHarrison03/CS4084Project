package com.example.myapplication;
import java.util.Date;
public class Reminder {
    private String description;
    private String title;
    private Date dateInput;
    private boolean canSend;

    public Reminder(String description, String title, Date dateInput, boolean canSend){
        this.description = description;
        this.dateInput = dateInput;
        this.canSend = canSend;
        this.title = title;
    }
}
