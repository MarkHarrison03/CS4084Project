package com.example.reminderapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    ProgressBar progressBar;
    Button changePasswordButton;
    EditText edtEmail;
    String strEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button changePasswordButton = findViewById(R.id.changePasswordButton);
        Button changeEmailButton = findViewById(R.id.changeEmailButton);
        ImageButton backButton = findViewById(R.id.backButton);
        edtEmail = findViewById(R.id.emailEditText);
        mAuth = FirebaseAuth.getInstance();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ProfileActivity", "Change password button clicked");
                strEmail = edtEmail.getText().toString().trim();
                if (!TextUtils.isEmpty(strEmail)){
                    changePassword();
                }else{
                    edtEmail.setError("Email field can't be empty");
                }
            }
        });

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeEmail();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });



    }



    private void changeUsername() {

    }

    private void changePassword() {
        Log.d("ProfileActivity", "changePassword method called");
        changePasswordButton.setVisibility(View.INVISIBLE);
        edtEmail.setVisibility(View.VISIBLE);
        strEmail = edtEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(strEmail)){
            mAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>(){
                @Override
                public void onSuccess(Void unused){
                    Toast.makeText(ProfileActivity.this, "Reset Password Link has been sent to your Email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Failed to send reset password link: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Show the changePasswordButton again if sending the reset password email fails
                    changePasswordButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            edtEmail.setError("Email field can't be empty");
            // Show the changePasswordButton again if the email field is empty
            changePasswordButton.setVisibility(View.VISIBLE);
        }
    }


    private void changeEmail() {

    }

    private void backButton() {
        Intent mainIntent = new Intent(ProfileActivity.this , MainActivity.class);
        startActivity(mainIntent);
    }
}






/*
ChatGpt for firebase stuff, i have no idea
private void changeUsername(String newUsername) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.child("username").setValue(newUsername)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Username updated successfully
                        Toast.makeText(ProfileActivity.this, "Username updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update username
                        Toast.makeText(ProfileActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                    }
                });

 */