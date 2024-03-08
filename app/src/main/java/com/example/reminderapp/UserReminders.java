package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserReminders extends AppCompatActivity {

    private ArrayList<Reminder> remindersList;
    private ArrayAdapter<Reminder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reminder);

        ListView listView = findViewById(R.id.listView);

        // Initialize the remindersList with an empty ArrayList
        remindersList = new ArrayList<>();

        // Initialize Adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, remindersList);

        // Set Adapter
        databaseQuery();


        listView.setAdapter(adapter);
    }


    public void databaseQuery(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference remindersRef = database.getReference("reminders");

        Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                remindersList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reminder reminder = snapshot.getValue(Reminder.class);
                    remindersList.add(reminder);
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(UserReminders.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
