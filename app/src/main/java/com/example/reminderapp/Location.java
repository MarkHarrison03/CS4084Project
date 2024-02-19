package com.example.reminderapp;

public class Location {
        private String nickname;
        private String address;
        private double latitude;
        private double longitude;

        // Constructor
        public Location(String nickname, String address, double latitude, double longitude) {
            this.nickname = nickname;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
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

        // toString method to display the Location information
        @Override
        public String toString() {
            return "Location{" +
                    "nickname='" + nickname + '\'' +
                    ", address='" + address + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }



