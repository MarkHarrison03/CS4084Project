package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserReminders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reminder);
        new DatabaseQueryTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class DatabaseQueryTask extends AsyncTask<Void, Void, ArrayList<Reminder>> {

        @Override
        protected ArrayList<Reminder> doInBackground(Void... voids) {
            ArrayList<Reminder> reminderList = new ArrayList<>();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference remindersRef = database.getReference().child("reminders");

            Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        reminderList.add(Utils.parseDataToReminder(snapshot.toString()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(UserReminders.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            return reminderList;
        }

        @Override
        protected void onPostExecute(ArrayList<Reminder> reminderList) {
            displayReminders(reminderList);
        }
    }

    public void displayReminders(ArrayList<Reminder> reminderList) {
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<Reminder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderList);
        listView.setAdapter(adapter);
    }
}