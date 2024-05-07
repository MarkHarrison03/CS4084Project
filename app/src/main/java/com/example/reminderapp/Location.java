package com.example.reminderapp;

public class Location {
    private String nickname;
    private String address;
    private double latitude;
    private double longitude;
    private int accuracyRadius; //determines the accuracy of the reminder by setting a radius around the location
    private String placeName;
    private String email;

    public Location() {
    }
    // Constructor
    public Location(String nickname, String address, double latitude, double longitude, int accuracyRadius, String email) {
        this.nickname = nickname;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracyRadius = accuracyRadius;
        this.email = email;
    }



    public String getPlaceName() {
        return this.placeName;
    }

    // Getters and setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAccuracyRadius() {
        return accuracyRadius;
    }

    public void setAccuracyRadius(int accuracyRadius) {
        this.accuracyRadius = accuracyRadius;
    }

    // toString method to display the Location information
    @Override
    public String toString() {
        String s = "nickname " + nickname + "\nlat: " + latitude + "\nlong: " + longitude + "\nradius: " + accuracyRadius;
        return s;

    }

}



