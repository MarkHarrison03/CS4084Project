    package com.example.reminderapp;

    import java.util.ArrayList;
    import java.util.Objects;

    public class User {
        private String firstName;
        private String lastName;
        private String userName;
        private ArrayList<Reminder> userReminders;
        private ArrayList<Location> userLocations;
        private ArrayList<Reminder> todayReminders;



        public User(String firstName, String lastName, String userName, ArrayList<Reminder> userReminders, ArrayList<Location> userLocations) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
            this.userReminders = userReminders;
            this.userLocations = userLocations;
        }

        public ArrayList<Reminder> getUserReminders() {
            return userReminders;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUserName() {
            return userName;
        }

        public String getFirstName() {
            return firstName;
        }

        public ArrayList<Location> getUserLocations() {
            return userLocations;
        }

        public void setUserReminders(ArrayList<Reminder> userReminders) {
            this.userReminders = userReminders;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setUserLocations(ArrayList<Location> userLocations) {
            this.userLocations = userLocations;
        }

        public void addUserReminder(Reminder reminder) {
            userReminders.add(reminder);
        }

        public void removeReminder(Reminder reminder) {
            userReminders.remove(reminder);
            userReminders.removeIf(Objects::isNull);
        }

        public void addUserLocation(Location l) {
            userLocations.add(l);
        }

        public void removeUserLocation(Location l) {
            userLocations.remove(l);
            userLocations.removeIf(Objects::isNull);
        }

        public ArrayList<Reminder> getTodayReminders(){
            return todayReminders;
        }

        public void addTodayReminder(Reminder reminder){
            todayReminders.add(reminder);
        }

        public void removeTodayReminder(Reminder reminder) {
            todayReminders.remove(reminder);
            todayReminders.removeIf(Objects::isNull);
        }
    }
