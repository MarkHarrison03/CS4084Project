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
        private String userID;


        public Reminder(String description, String title, LocalDateTime dateInput, String userID){
            this.description = description;
            this.dateInput = dateInput;
            this.title = title;
            this.userID = userID;
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

        public String getUserID(){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                return user.toString();
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

        public void reminderParser(ArrayList<Reminder> reminders){
            for (Reminder r:reminders){
                activateReminder(r);
            }
        }

        //Method to get today's reminders and add them to a separate list to save on cpu and memory usage
        public void todayReminders(ArrayList<Reminder> reminders, User user){
            LocalDate currentDate = LocalDate.now();
            reminders = user.getUserReminders();
            for(Reminder r: reminders){
                if(r.getDateInput().toLocalDate().equals(currentDate)){
                    user.addTodayReminder(r);
                }
            }
            //Checking if there are any reminders not for today's date in here and removing them
            for (Reminder rem: user.getTodayReminders()){
                if (!(rem.getDateInput().toLocalDate().equals(currentDate))){
                    user.removeTodayReminder(rem);
                }
            }
        }

    }
