package com.example.reminderapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
public class Utils {

    public static Reminder parseDataToReminder(String data) {
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
        Log.d("DATETIME", datetime.toString());
        Reminder newReminder = new Reminder(description, title, datetime);
        return newReminder;
    }
}
