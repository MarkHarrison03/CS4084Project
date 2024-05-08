package com.example.reminderapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class newLocation_dialog {
    private Location tempLocation;
    private String locationNickname;
    private int accuracyRadius = 10;

    public void showDialog(Activity activity, FragmentActivity fragmentActivity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.newlocation_dialog, null);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", (dialog, id) -> {
            EditText locationNicknameEditText = dialogView.findViewById(R.id.location_nickname);
            locationNickname = locationNicknameEditText.getText().toString();

            if (tempLocation != null) {
                tempLocation.setNickname(locationNickname);
            }
            Singleton.getInstance().setTempLocation(tempLocation);


        }).setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        SeekBar accuracyRadiusSeekBar = dialogView.findViewById(R.id.accuracy_radius_seekbar);
        TextView accuracyRadiusValueTextView = dialogView.findViewById(R.id.accuracy_radius_value);

        accuracyRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                accuracyRadius = progress;
                accuracyRadiusValueTextView.setText("Accuracy Radius: " + progress + " meters");

                if (tempLocation != null) {
                    tempLocation.setAccuracyRadius(accuracyRadius);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    String userEmail = Singleton.getInstance().getUserEmail();

                    tempLocation = new Location("", place.getAddress(), latitude, longitude, accuracyRadius, userEmail);

                } else {
                    Log.e(TAG, "LatLng object is null for place: " + place.getName());
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(dialogInterface -> {
            if (com.google.android.libraries.places.api.Places.isInitialized()) {
                com.google.android.libraries.places.api.Places.deinitialize();
            }
        });
        dialog.show();
    }
}
