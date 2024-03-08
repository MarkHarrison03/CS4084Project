package com.example.reminderapp;

public class Singleton {
    private  String currentUserEmail;
    private static Singleton instance;

    private Singleton() {
        this.currentUserEmail = null;
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void setCurrentUserEmail(String currentUserEmail){
        this.currentUserEmail = currentUserEmail;
    }
    public String getUserEmail() {
        return currentUserEmail;
    }
}
