package com.example.reminderapp;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;


public class newLocation_dialog {

    public static void showDialog(Context context, FragmentActivity fragmentActivity, List<String> locationsList, Spinner locationsSpinner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = fragmentActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.newlocation_dialog, null);
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText locationNicknameEditText = dialogView.findViewById(R.id.location_nickname);
                        String locationNickname = locationNicknameEditText.getText().toString();

                        String locationData = ""; // Replace this with the actual location data
                        locationsList.add(locationData + " (" + locationNickname + ")");

                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) locationsSpinner.getAdapter();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle negative button click
                        dialog.cancel();
                    }
                });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String placeName = place.getName();
                String placeAddress = place.getAddress();
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    Log.i(TAG, "Place: " + placeName + ", Lat: " + latitude + ", Lng: " + longitude);

                    Location selectedLocation = new Location(placeName, placeAddress, latitude, longitude, 0);

                    Log.d(TAG, "Selected Location: " + selectedLocation.toString());
                } else {
                    Log.e(TAG, "LatLng object is null for place: " + placeName);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

