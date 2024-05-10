package com.example.reminderapp;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent notificationSchedule = new Intent(this, NotificationScheduleService.class);
        startService(notificationSchedule);

        Intent notificationSending = new Intent(this, NotificationSendingService.class);
        startService(notificationSending);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        boolean areNotificationsEnabled = notificationManager.areNotificationsEnabled();
        System.out.println("NOTIFS: " + areNotificationsEnabled);
        if(!areNotificationsEnabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enable Notifications");
            builder.setMessage("To receive your reminders as notifications, please enable notifications for this app.");
            builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
        boolean hasBackgroundPerms = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if(!hasBackgroundPerms){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enable Notifications");
            builder.setMessage("To receive your location reminders when the app is closed , please enable 'All the time'  for the location settings for this app.");
            builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,

        };

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                finish();
                startActivity(getIntent());
            }
        });


        databaseQuery();

        for(Reminder r : Singleton.getInstance().getUserReminders()){
            Log.d("TODAY", r.toString());
        }

        String apiKey = BuildConfig.MAPS_API_KEY;

        if (TextUtils.isEmpty(apiKey)) {
            Log.e("Places test", "No api key");
            finish();
            return;
        }

        Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey);

        Places.createClient(this);

    }

    private boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Permission granted: " + permissions[i]);
                } else {
                    Log.d("MainActivity", "Permission denied: " + permissions[i]);
                   // System.exit(0);
                }
            }
        }
    }

    public void databaseQuery(){

        ArrayList<Reminder> reminderList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference remindersRef = database.getReference().child("reminders");
        Log.d("ReminderHelp", Singleton.getInstance().getUserEmail().toLowerCase());
        Query query = remindersRef.orderByChild("email").equalTo(Singleton.getInstance().getUserEmail().toLowerCase().trim());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    reminderList.add(Utils.parseDataToReminder(snapshot.toString()));
                }
               Log.d("ReminderHelp" , reminderList.toString());
                displayReminders(reminderList);
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MainActivity.this, "Failed to fetch reminders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void displayReminders(ArrayList<Reminder> reminderList){
//        ArrayList<Reminder> unsentReminders = new ArrayList<>();
//        for(Reminder a : reminderList){
//            if(!a.getIsSent()){
//                unsentReminders.add(a);
//                System.out.println("Reminder being added: " + a);
//            }
//        }
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<Reminder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderList);

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                handleLongPress(position);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked No button, dismiss the dialog
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog dialog = builder.create(); // Create and show the dialog
                dialog.show();

                return true;
            }
        });


    }
    private void handleLongPress(int pos){
        System.out.println("LOng press!");
        System.out.println(pos);
        ListView listView = findViewById(R.id.listView);
        ListAdapter adapter = listView.getAdapter();
        ArrayAdapter<Reminder> reminderAdapter = (ArrayAdapter<Reminder>) adapter;
        if (adapter != null && adapter instanceof ArrayAdapter<?>){
            System.out.println( adapter.getItem(pos));
            Reminder r = reminderAdapter.getItem(pos);
            Utils.deleteFromDB(r.getID());
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e ){
                Log.d("error", e.toString());
            }
            finish();
            startActivity(getIntent());
        }
        return;
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver", "Refresh action received");

            if ("refresh".equals(intent.getAction())) {
                System.out.println("HERE WE ARE. ONCE AGAIN");
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e ){
                    Log.d("error", e.toString());
                }
                finish();
                startActivity(getIntent());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("custom_action"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }





}

