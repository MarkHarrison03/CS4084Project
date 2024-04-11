package com.example.reminderapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewReminderActivity extends AppCompatActivity {

    String label;
    String description;
    int hour;
    int minute;
    int year;
    int month;
    int dayOfMonth;
    boolean isLocation = false;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = Singleton.getInstance().getUserEmail();


    String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        Log.d("NewReminder", "this mf making a reminder");


        List<String> locations_array = Arrays.asList("home", "school", "work");
        FirebaseApp.initializeApp(this);
        Spinner locations = (Spinner) findViewById(R.id.LocationSpinner);
        locations.setVisibility(View.GONE);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null && autocompleteFragment.getView() != null) {
            // Hide the AutocompleteSupportFragment
            autocompleteFragment.getView().setVisibility(View.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locations.setAdapter(adapter);

        CheckBox locationCheck = findViewById(R.id.LocationCheck);
        Button timeButton = findViewById(R.id.TimeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NewReminder", "Time button clicked");

                showTimePickerDialog();
            }
        });

        Button dateButton = findViewById(R.id.DateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NewReminder", "Date button clicked");

                showDatePickerDialog();
            }
        });


        locationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    if (autocompleteFragment != null && autocompleteFragment.getView() != null) {
                        autocompleteFragment.getView().setVisibility(View.VISIBLE);
                    }

                    locations.setVisibility(View.VISIBLE);
                    isLocation = true;
                } else {
                    if (autocompleteFragment != null && autocompleteFragment.getView() != null) {
                        autocompleteFragment.getView().setVisibility(View.GONE);
                    }
                    locations.setVisibility(View.GONE);
                    isLocation = false;
                }
            }
        });


        Button submitbutton = findViewById(R.id.SubmitButton);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NewReminder", "Submit button clicked");

                submittedReminder();

            }
        });



        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String placeName = place.getName();
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    Log.i(TAG, "Place: " + placeName + ", Lat: " + latitude + ", Lng: " + longitude);

                } else {
                    Log.e(TAG, "LatLng object is null for place: " + placeName);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
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

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                year = selectedYear;
                month = selectedMonth + 1;
                dayOfMonth = selectedDayOfMonth;
                Log.d("Monthcheck", String.valueOf(month));

                for (Reminder r : Singleton.getInstance().getUserReminders()) {
                    Log.d("TODAYREMINDER", r.toString());
                }
                // Set the date on the dateButton
                Button dateButton = findViewById(R.id.DateButton);
                dateButton.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month, year));
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewReminderActivity.this, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void submittedReminder() {


        EditText Label = (EditText) findViewById(R.id.LabelText);
        label = Label.getText().toString();

        EditText Description = (EditText) findViewById(R.id.DescriptionText);
        description = Description.getText().toString();

        Spinner location_spinner = (Spinner) findViewById(R.id.LocationSpinner);
        String location = location_spinner.getSelectedItem().toString();


        LocalDateTime newReminderTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);


        Reminder newReminder = new Reminder(description, label, newReminderTime);
        Log.d("year", String.valueOf(newReminder.getDateInput().getMonth()));


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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NewReminder", "Firebase push failed");
                    }
                });


    }

}



