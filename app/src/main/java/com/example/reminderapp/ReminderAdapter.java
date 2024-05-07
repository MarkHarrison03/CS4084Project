package com.example.reminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private Context mContext;
    private List<Reminder> mReminders;

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        super(context, 0, reminders);
        mContext = context;
        mReminders = reminders;
    }

    @NonNull

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        Reminder reminder = mReminders.get(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(reminder.getTitle());

        return convertView;
    }
}