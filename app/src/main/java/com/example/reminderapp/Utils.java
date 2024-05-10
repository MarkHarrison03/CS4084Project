package com.example.reminderapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Utils {

    public static void deleteFromDB(long reminderID){

        DatabaseReference remindersRef = FirebaseDatabase.getInstance().getReference("reminders");
        remindersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot reminderSnapshot : dataSnapshot.getChildren()) {
                        Reminder r = Utils.parseDataToReminder(reminderSnapshot.toString());
                        System.out.println("ID: " + r.getID());
                    if (r.getID() == reminderID){
                        if (r != null && r.getEmail().equals(Singleton.getInstance().getUserEmail())) {
                            reminderSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Delete Rem", "Reminder deleted successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Delete Rem", "Error deleting reminder", e);
                                        }
                                    });
                        } else {
                            Log.d("Delete Rem", "Email does not match, cannot delete reminder");
                        }
                        return; // Exit loop after finding the reminder
                    }
                }
                // If the loop finishes without finding the reminder
                Log.d("Delete Rem", "Reminder with ID " + reminderID + " does not exist");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Delete Rem", "Database error occurred", databaseError.toException());
            }
        });
        }

    public static Reminder parseDataToReminder(String data) {
        ArrayList<Reminder> reminderList = new ArrayList<>();
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("JANUARY", 1);
        monthMap.put("FEBRUARY", 2);
        monthMap.put("MARCH", 3);
        monthMap.put("APRIL", 4);
        monthMap.put("MAY", 5);
        monthMap.put("JUNE", 6);
        monthMap.put("JULY", 7);
        monthMap.put("AUGUST", 8);
        monthMap.put("SEPTEMBER", 9);
        monthMap.put("OCTOBER", 10);
        monthMap.put("NOVEMBER", 11);
        monthMap.put("DECEMBER", 12);

        String[] splitString = data.split("[=,{}]");
        String description = "";
        String title = "";
        String email = "";
        Location location = null;
        LocalDateTime datetime = null;
        long id = 0;
        LocalDateTime endDate = null;
        for (int i = 0; i < splitString.length; i++) {
            System.out.println("details:" + splitString[i]);


            if(splitString[i].equals("description") ||splitString[i].equals(" description") ){
                description = splitString[i+1];
            }
            if(splitString[i].equals(" title")){
                title = splitString[i+1];
            }
            if(splitString[i].equals(" dateInput")){
                datetime = LocalDateTime.of(Integer.parseInt(splitString[i+13]), monthMap.get(splitString[i+7]).intValue(), Integer.parseInt(splitString[i+9]), Integer.parseInt(splitString[i+5]), Integer.parseInt(splitString[i+26]), Integer.parseInt(splitString[i+28]));
            }
            if(splitString[i].equals("endDate")){
                System.out.println("ARWFNOWAF");
                endDate = LocalDateTime.of(Integer.parseInt(splitString[i+13]), monthMap.get(splitString[i+7]).intValue(), Integer.parseInt(splitString[i+9]), Integer.parseInt(splitString[i+5]), Integer.parseInt(splitString[i+26]), Integer.parseInt(splitString[i+28]));
            }

            if(splitString[i].equals(" email")){
                email = splitString[i+1];
            }

            if(splitString[i].equals(" location") ||splitString[i].equals("location")){
                location = new Location(splitString[i+7], "", Double.parseDouble(splitString[i+5]), Double.parseDouble(splitString[i+9]), Integer.parseInt(splitString[i+3]), Singleton.getInstance().getUserEmail());
            }
            if(splitString[i].equals(" id")){
               try{ id = Long.parseLong(splitString[i+1]);
               }catch (NumberFormatException e){
                   Log.d("log", "wrong num");
               }
            }

        }
        Reminder newReminder;
        if(location != null) {
             newReminder = new Reminder(description, title, datetime,location, endDate,id);
        }else{
             newReminder = new Reminder(description, title, datetime,id);
        }
        System.out.println("AAAAAAAAAAAAA" + newReminder.getEndDate());
        return newReminder;
    }
}
