    package com.example.reminderapp;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import android.os.Bundle;
    import android.widget.ListView;

    import com.google.firebase.FirebaseApp;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.List;

    public class ViewReminderActivity extends AppCompatActivity {


        private ReminderAdapter mReminderAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_reminder);

            ListView mReminderListView = findViewById(R.id.ReminderList);
            mReminderAdapter = new ReminderAdapter(this, new ArrayList<>());
            mReminderListView.setAdapter(mReminderAdapter);

            FirebaseApp.initializeApp(this);
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://cs4084project-6f69d-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference mRemindersRef = database.getReference("reminders");
            System.out.println("HELLO FROM VIEW REMINDERS");
            mRemindersRef.addValueEventListener(new ValueEventListener() {

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Reminder> reminders = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Reminder reminder = snapshot.getValue(Reminder.class);
                        System.out.println(reminder);
                        if (reminder != null) {
                            reminders.add(reminder);
                        }
                    }
                    mReminderAdapter.clear();
                    mReminderAdapter.addAll(reminders);
                    mReminderAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }