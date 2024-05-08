package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewReminderActivity extends AppCompatActivity {

    String label;
    String description;
    int hour;
    int minute;
    int year;
    int month;
    int dayOfMonth;
    boolean isLocation = false;
    boolean isDateSet = false;
    boolean isTimeSet = false;
    Button submitButton;
    List<Location> locations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        submitButton = findViewById(R.id.SubmitButton);


        Location savedLocations = new Location();
        savedLocations.setNickname("Saved Locations");
        locations.add(savedLocations);

        Location newLocation = new Location();
        newLocation.setNickname("New Location");
        locations.add(newLocation);


        CheckBox locationCheck = findViewById(R.id.LocationCheck);
        Button timeButton = findViewById(R.id.TimeButton);
        timeButton.setOnClickListener(view -> {
            Log.d("NewReminder", "Time button clicked");

            showTimePickerDialog();
        });

        Button dateButton = findViewById(R.id.DateButton);
        dateButton.setOnClickListener(view -> {
            Log.d("NewReminder", "Date button clicked");

            showDatePickerDialog();
        });

        locationCheck.setOnClickListener(view -> {
            if (((CheckBox) view).isChecked()) {
                isLocation = true;
                newLocation_dialog dialog_newLocation = new newLocation_dialog();
                dialog_newLocation.showDialog(NewReminderActivity.this, NewReminderActivity.this);
            } else {
                isLocation = false;
            }
        });

        submitButton.setOnClickListener(view -> {
            Log.d("NewReminder", "Submit button clicked");
            if (isDateSet && isTimeSet) {
                submitButton.setEnabled(false);
                submittedReminder();
            } else {
                Toast.makeText(NewReminderActivity.this, "Please select a date and time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                // Set the time on the timeButton
                Button timeButton = findViewById(R.id.TimeButton);
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                isTimeSet = true;
                if (isDateSet) {
                    submitButton.setEnabled(true);
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(NewReminderActivity.this, onTimeSetListener, hour, minute, true);
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            year = selectedYear;
            month = selectedMonth + 1;
            dayOfMonth = selectedDayOfMonth;

            Button dateButton = findViewById(R.id.DateButton);
            dateButton.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month, year));
            isDateSet = true;
            if (isTimeSet) {
                submitButton.setEnabled(true);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewReminderActivity.this, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void submittedReminder() {

        CheckBox locationCheck = findViewById(R.id.LocationCheck);


        EditText Label = findViewById(R.id.LabelText);
        label = Label.getText().toString();

        EditText Description = findViewById(R.id.DescriptionText);
        description = Description.getText().toString();

        Reminder newReminder;
        LocalDateTime newReminderTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);


        if (locationCheck.isChecked()) {
            Location selectedLocation = Singleton.getInstance().getTempLocation();
            newReminder = new Reminder(description, label, newReminderTime, selectedLocation);
        } else {
            newReminder = new Reminder(description, label, newReminderTime);
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://cs4084project-6f69d-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference remindersRef = database.getReference("reminders");
        Log.d("AUTH", Singleton.getInstance().getUserEmail());
        System.out.println(Singleton.getInstance().getUserEmail());
        // Push the new reminder object to the database
        DatabaseReference newReminderRef = remindersRef.push();
        newReminderRef.setValue(newReminder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("NewReminder", "Firebase push success");
                        Intent newRemind = new Intent(NewReminderActivity.this, MainActivity.class);
                        startActivity(newRemind);

                        submitButton.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NewReminder", "Firebase push failed");
                        submitButton.setEnabled(true);
                    }
                });

        if (!isDateSet || !isTimeSet) {
            Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.deinitialize();
        }
    }
}