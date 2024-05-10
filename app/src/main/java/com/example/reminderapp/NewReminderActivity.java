package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
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

    int endYear;
    int endMonth;
    int endDayOfMonth;
    int endHour;
    int endMinute;
    boolean isEndDateSet = false;
    boolean isEndTimeSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        submitButton = findViewById(R.id.SubmitButton);


        Button endDateButton = findViewById(R.id.EndDateButton);
        endDateButton.setVisibility(View.GONE);
        endDateButton.setOnClickListener(view -> {
            showEndDatePickerDialog();
        });
        Button endTimeButton = findViewById(R.id.EndTimeButton);
        endTimeButton.setVisibility(View.GONE);
        endTimeButton.setOnClickListener(view -> {
            showEndTimePickerDialog();
        });

        CheckBox locationCheck = findViewById(R.id.LocationCheck);


        Location savedLocations = new Location();
        savedLocations.setNickname("Saved Locations");
        locations.add(savedLocations);

        Location newLocation = new Location();
        newLocation.setNickname("New Location");
        locations.add(newLocation);

        Button timeButton = findViewById(R.id.TimeButton);
        timeButton.setOnClickListener(view -> {
            showTimePickerDialog();
        });

        Button dateButton = findViewById(R.id.DateButton);
        dateButton.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        locationCheck.setOnClickListener(view -> {
            String apiKey = BuildConfig.MAPS_API_KEY;

            if (TextUtils.isEmpty(apiKey)) {
                Log.e("Places test", "No api key");
                finish();
                return;
            }
//            if(!com.google.android.libraries.places.api.Places.isInitialized()){
//                System.out.println("HEYO back up na drunning ");
//                Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey) ;
//                System.out.println("HEYO " + com.google.android.libraries.places.api.Places.isInitialized());
//            };

            if (((CheckBox) view).isChecked()) {
                isLocation = true;
                newLocation_dialog dialog_newLocation = new newLocation_dialog();
                dialog_newLocation.showDialog(NewReminderActivity.this, NewReminderActivity.this);
                endDateButton.setVisibility(View.VISIBLE);
                endTimeButton.setVisibility(View.VISIBLE);
            } else {
                finish();
                startActivity(getIntent());

                isLocation = false;
                endDateButton.setVisibility(View.GONE);
                endTimeButton.setVisibility(View.GONE);
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
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            // Set the time on the timeButton
            Button timeButton = findViewById(R.id.TimeButton);
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            isTimeSet = true;
            if (isDateSet && !isLocation) {
                submitButton.setEnabled(true);
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
            if (isTimeSet && !isLocation) {
                submitButton.setEnabled(true);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewReminderActivity.this, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showEndTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            endHour = selectedHour;
            endMinute = selectedMinute;
            Button timeButton = findViewById(R.id.EndTimeButton);
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour,endMinute));
            isEndTimeSet = true;
            if (isEndDateSet && isTimeSet && isDateSet) {
                submitButton.setEnabled(true);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(NewReminderActivity.this, onTimeSetListener, hour, minute, true);
        timePickerDialog.show();
    }

    private void showEndDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            endYear = selectedYear;
            endMonth = selectedMonth + 1;
            endDayOfMonth = selectedDayOfMonth;

            Button dateButton = findViewById(R.id.EndDateButton);
            dateButton.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", endDayOfMonth, endMonth, endYear));
            isEndDateSet = true;
            if (isEndTimeSet && isDateSet && isTimeSet) {
                submitButton.setEnabled(true);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewReminderActivity.this, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void submittedReminder() {

        CheckBox locationCheck = findViewById(R.id.LocationCheck);


        EditText Label = findViewById(R.id.LabelText);
        label = Label.getText().toString();

        EditText Description = findViewById(R.id.DescriptionText);
        description = Description.getText().toString();

        Reminder newReminder;
        LocalDateTime newReminderTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);

        if(newReminderTime.isBefore(LocalDateTime.now())){
            Toast.makeText(this, "Date and Time selected is in the past" , Toast.LENGTH_SHORT).show();
            return;
        }

        if (locationCheck.isChecked()) {
            Location selectedLocation = Singleton.getInstance().getTempLocation();
            LocalDateTime reminderEndTime = LocalDateTime.of(endYear, endMonth, endDayOfMonth, endHour, endMinute);
            if(reminderEndTime.isBefore(newReminderTime)){
                Toast.makeText(this, "End Date and Time selected is in the past" , Toast.LENGTH_SHORT).show();
                return;
            }
                newReminder = new Reminder(description, label, newReminderTime, selectedLocation, reminderEndTime);

        } else {
            newReminder = new Reminder(description, label, newReminderTime);
        }

        if ( !isLocation  && !isDateSet || !isTimeSet) {
            Toast.makeText(this, "Please select a date and timel", Toast.LENGTH_SHORT).show();
            return;
        } else if (isLocation && (!isEndDateSet || !isEndTimeSet)){
            Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://cs4084project-6f69d-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference remindersRef = database.getReference("reminders");
        DatabaseReference newReminderRef = remindersRef.push();
        newReminderRef.setValue(newReminder)
                .addOnSuccessListener(aVoid -> {
                    Log.d("NewReminder", "Firebase push success");
                    Intent newRemind = new Intent(NewReminderActivity.this, MainActivity.class);
                    startActivity(newRemind);

                    submitButton.setEnabled(true);
                })
                .addOnFailureListener(e -> {
                    Log.d("NewReminder", "Firebase push failed");
                    submitButton.setEnabled(true);
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.deinitialize();
        }
    }
}