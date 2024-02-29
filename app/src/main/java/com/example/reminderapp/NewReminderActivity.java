package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Locale;

public class NewReminderActivity extends AppCompatActivity {


    int hour;
    int minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("NewReminder", "This mf making a reminder");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);


        Button timeButton = findViewById(R.id.TimeButton);
        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("NewReminder", "This mf making a reminder");

                TimePickerDialog.OnTimeSetListener onTimeSetListener =new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));

                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewReminderActivity.this, onTimeSetListener, hour, minute, true);
                timePickerDialog.show();




            }





});
}}
