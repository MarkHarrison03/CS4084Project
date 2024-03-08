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
        private String email = Singleton.getInstance().getUserEmail();


        public Reminder(String description, String title, LocalDateTime dateInput, String email){
            this.description = description;
            this.dateInput = dateInput;
            this.title = title;
            this.email = email;
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

        public String getEmail(){
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

        /*
        public void activateReminder(Reminder reminder) {
            reminder.setCanSend(reminder.dateInput == LocalDateTime.now());
        }

        public void ReminderParser(ArrayList<Reminder> reminders, User user){
            reminders = user.getUserReminders();
            for (Reminder r:reminders){
                activateReminder(r);
            }
        }
        */

    }
