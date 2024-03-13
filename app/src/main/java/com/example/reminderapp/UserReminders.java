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
import java.util.ArrayList;

public class UserReminders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reminder);

        ListView listView = findViewById(R.id.listView);

        // Initialize Adapter
        ArrayList<Reminder> remindersList = databaseQuery();
        System.out.println(remindersList);
//
//        ArrayAdapter<Reminder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, remindersList);
//
//        listView.setAdapter(adapter);
    }


    public ArrayList<Reminder> databaseQuery(){
        ArrayList<Reminder> reminderList  = new ArrayList<Reminder>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference remindersRef = database.getReference("reminders");

        Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail());
        Log.d("RETRIEVE", "test1");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("RETRIEVE", "test2");


                    Reminder reminder = dataSnapshot.getValue(Reminder.class);
                    Log.d("RETRIEVE", reminder.getTitle());
                    reminderList.add(reminder);

            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(UserReminders.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return reminderList;
    }
}
