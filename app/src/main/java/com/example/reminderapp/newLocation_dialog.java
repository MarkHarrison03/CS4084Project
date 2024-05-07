package com.example.reminderapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
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
import java.util.Collections;
import java.util.List;

public class newLocation_dialog {
    private Location tempLocation;
    private String locationNickname;
    private static String selectedLocationData;
    int accuracyRadius = 10;
    private Context context;
    private List<Location> locations = new ArrayList<>(); // Add this line
    private FragmentActivity fragmentActivity;

    public void showDialog(Activity activity, FragmentActivity fragmentActivity, List<Location> locations, Spinner locationsSpinner) {

        this.context = activity;
        this.fragmentActivity = fragmentActivity;
        this.locations = locations;

        ArrayAdapter<Location> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationsSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.newlocation_dialog, null);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText locationNicknameEditText = dialogView.findViewById(R.id.location_nickname);
                        locationNickname = locationNicknameEditText.getText().toString();

                        if (tempLocation != null) {
                            tempLocation.setNickname(locationNickname);
                            adapter.notifyDataSetChanged();
                        }


                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://cs4084project-6f69d-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference locationsRef = database.getReference("locations");
                        String userId = Singleton.getInstance().getCurrentUserId();

                        DatabaseReference userLocationsRef = locationsRef.child(userId);
                        String userEmail = Singleton.getInstance().getUserEmail();

                        userLocationsRef.push().setValue(tempLocation)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("NewLocation", "Firebase push success");
                                        locations.add(tempLocation);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("NewLocation", "Firebase push failed");
                                    }
                                });


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        SeekBar accuracyRadiusSeekBar = dialogView.findViewById(R.id.accuracy_radius_seekbar);
        TextView accuracyRadiusValueTextView = dialogView.findViewById(R.id.accuracy_radius_value);

        accuracyRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                accuracyRadius = progress;
                accuracyRadiusValueTextView.setText("Accuracy Radius: " + progress + " meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    String userEmail = Singleton.getInstance().getUserEmail();

                    adapter.notifyDataSetChanged();
                    tempLocation = new Location("", place.getAddress(), latitude, longitude, accuracyRadius, userEmail);

                    selectedLocationData = place.getName() + " (" + place.getAddress() + ")";
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (com.google.android.libraries.places.api.Places.isInitialized()) {
                    com.google.android.libraries.places.api.Places.deinitialize();
                }
            }
        });
        dialog.show();
    }


}
