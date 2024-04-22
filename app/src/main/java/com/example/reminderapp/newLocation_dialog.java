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

    private String locationNickname;
    private static String selectedLocationData;
    int accuracyRadius = 0;

    public void showDialog(Context context, FragmentActivity fragmentActivity, Spinner locationsSpinner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = fragmentActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.newlocation_dialog, null);

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText locationNicknameEditText = dialogView.findViewById(R.id.location_nickname);
                        locationNickname = locationNicknameEditText.getText().toString();
                        updateSpinner(locationsSpinner, locationNickname);
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
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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

                    Location location = new Location(locationNickname, place.getAddress(), latitude, longitude, accuracyRadius);
                    Singleton.getInstance().addLocation(location);
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://cs4084project-6f69d-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference locationsRef = database.getReference("locations");
                    String userId = Singleton.getInstance().getCurrentUserId();
                    DatabaseReference userLocationsRef = locationsRef.child(userId);
                    userLocationsRef.push().setValue(location)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("NewLocation", "Firebase push success");
                                    updateSpinner(locationsSpinner, locationNickname);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("NewLocation", "Firebase push failed");
                                }
                            });

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
        dialog.show();
    }

    private void updateSpinner(Spinner locationsSpinner, String newLocation) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) locationsSpinner.getAdapter();
        List<String> itemList = new ArrayList<>();
        List<Location> locations = Singleton.getInstance().getCurrentUserLocationsForReminders();

        if (locations != null) {
            for (Location location : locations) {
                String nickname = location.getNickname();
                if (nickname != null) {
                    itemList.add(nickname);
                }
            }
        }
        itemList.add(newLocation);
        adapter.clear();
        adapter.addAll(itemList);
        adapter.notifyDataSetChanged();
    }
}
