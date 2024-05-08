package com.example.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navbar extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navbar, container, false);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.add) {
                    startActivity(new Intent(getActivity(), NewReminderActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        return view;
    }
}