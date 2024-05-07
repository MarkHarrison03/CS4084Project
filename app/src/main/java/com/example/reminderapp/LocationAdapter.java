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

public class LocationAdapter extends ArrayAdapter<Location> {
    public LocationAdapter(Context context, List<Location> locations) {
        super(context, 0, locations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(android.R.id.text1);
        Location location = getItem(position);

        if (location != null) {
            String nickname = location.getNickname();
            if (nickname != null && !nickname.isEmpty()) {
                txtTitle.setText(nickname);
            } else {
                String placeName = location.getPlaceName();
                String address = location.getAddress();
                if (placeName != null && !placeName.isEmpty()) {
                    txtTitle.setText(placeName);
                } else if (address != null && !address.isEmpty()) {
                    txtTitle.setText(address);
                } else {
                    txtTitle.setText("");
                }
            }
        }
        return convertView;
    }
}