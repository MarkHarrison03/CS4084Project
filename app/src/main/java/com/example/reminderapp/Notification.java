package com.example.reminderapp;

import java.time.LocalDateTime;

public class Notification {
    private String message;
    private LocalDateTime time;

    public Notification(String message, LocalDateTime time){
        this.message = message;
        this.time = time;
    }
    public Notification(String message){
        this.message = message;
    }

    public   void sendNotification(){

    }

    public static void main(String[] args) {

    }
}
