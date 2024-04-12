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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class newLocation_dialog {

    private String locationNickname;
    private static String selectedLocationData;
    int accuracyRadius = 0;

    public void showDialog(Context context, FragmentActivity fragmentActivity, List<String> locationsList, Spinner locationsSpinner) {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate the dialog layout
        LayoutInflater inflater = fragmentActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.newlocation_dialog, null);

        // Set up the positive button (OK)
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText locationNicknameEditText = dialogView.findViewById(R.id.location_nickname);
                        locationNickname = locationNicknameEditText.getText().toString();

                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) locationsSpinner.getAdapter();
                        List<String> itemList = new ArrayList<>();
                        for(int i=0; i<adapter.getCount();i++){
                            itemList.add(adapter.getItem(i));
                        }

                        itemList.add(locationNickname);  //Add new item

                        adapter.clear();
                        adapter.addAll(itemList);

                        adapter.notifyDataSetChanged();
                    }
                })

                // Set up the negative button (Cancel)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle negative button click
                        dialog.cancel();
                    }
                });

        // Get references to UI elements in the dialog view
        SeekBar accuracyRadiusSeekBar = dialogView.findViewById(R.id.accuracy_radius_seekbar);
        TextView accuracyRadiusValueTextView = dialogView.findViewById(R.id.accuracy_radius_value);

        // Set a listener on the seek bar to update the text of the accuracy radius value
        accuracyRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                accuracyRadius = progress;
                accuracyRadiusValueTextView.setText("Accuracy Radius: " + progress + " meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify which fields you want to retrieve when selecting a place
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Add a listener that will be called when a place is selected
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String placeName = place.getName();
                String placeAddress = place.getAddress();
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;

                    Location location = new Location(locationNickname, placeAddress, latitude, longitude, accuracyRadius);

                    selectedLocationData = placeName + " (" + placeAddress + ")";
                } else {
                    Log.e(TAG, "LatLng object is null for place: " + placeName);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}