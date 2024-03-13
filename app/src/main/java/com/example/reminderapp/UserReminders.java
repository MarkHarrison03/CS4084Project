package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserReminders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reminder);


    }

    public void displayReminders(){
        ListView listView = findViewById(R.id.listView);
        System.out.println("AAAAAAAAAAAAAAA");
        // Initialize Adapter
        ArrayList<Reminder> remindersList = databaseQuery();
        System.out.println(remindersList);

        ArrayAdapter<Reminder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, remindersList);

        listView.setAdapter(adapter);
    }

    public ArrayList<Reminder> databaseQuery(){
        ArrayList<Reminder> reminderList  = new ArrayList<Reminder>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference remindersRef = database.getReference().child("reminders");

        Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail());
        Log.d("RETRIEVE", Singleton.getInstance().getUserEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    Log.d("RETRIEVE", "test2");
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("RETRIEVE", "test3");
                    parseDataToReminder(snapshot.toString(),reminderList);
                   // displayReminders();
                    for(Reminder r : reminderList){
                        System.out.println(r);
                    }
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(UserReminders.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return reminderList;
    }

    public Reminder parseDataToReminder(String data, ArrayList<Reminder> reminders) {

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
        for(int i = 0; i < splitString.length; i++) {
            System.out.println(i + ":  " +  splitString[i] + "\n");
        }
        String description = "";
        String title = "";
        String email = "";
        LocalDateTime datetime = null;

        for (int i = 0; i < splitString.length; i++) {
            System.out.println(splitString[i]);
            if(splitString[i].equals("description")){
                 description = splitString[i+1];
            }
            if(splitString[i].equals(" title")){
                 title = splitString[i+1];
            }
            if(splitString[i].equals(" dateInput")){
                datetime = LocalDateTime.of(Integer.parseInt(splitString[i+13]), monthMap.get(splitString[i+7]).intValue(), Integer.parseInt(splitString[i+9]), Integer.parseInt(splitString[i+5]), Integer.parseInt(splitString[i+26]), Integer.parseInt(splitString[i+28]));
            }
            if(splitString[i].equals(" email")){
                email = splitString[i+1];
            }

        }
        Reminder newReminder = new Reminder(description, title, datetime);
        System.out.println(newReminder.getEmail() + " " + newReminder.getTitle() + " " + newReminder.getDescription() + " " + newReminder.getDateInput());
        reminders.add(newReminder);
        return null;
    }
    }

