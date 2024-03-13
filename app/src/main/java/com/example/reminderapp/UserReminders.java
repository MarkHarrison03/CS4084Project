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
        databaseQuery();

    }



    public void databaseQuery(){

        ArrayList<Reminder> reminderList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference remindersRef = database.getReference().child("reminders");

        Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    reminderList.add(parseDataToReminder(snapshot.toString()));
                }
                displayReminders(reminderList);
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(UserReminders.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public Reminder parseDataToReminder(String data) {
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
        return newReminder;
    }

    public void displayReminders(ArrayList<Reminder> reminderList){
        ListView listView = findViewById(R.id.listView);
        // Initialize Adapter
        ArrayAdapter<Reminder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderList);
        listView.setAdapter(adapter);
    }

}

