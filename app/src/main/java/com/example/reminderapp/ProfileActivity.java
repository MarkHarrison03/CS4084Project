package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

//    FirebaseAuth mAuth;
//    Button changePasswordButton;
//    Button changeEmailButton;
//    EditText edtEmail;
//    EditText passwordEditText;
//    Button submitResetEmail;
//    Button submitResetPassword;
//    EditText newEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//
//        changePasswordButton = findViewById(R.id.changePasswordButton);
//        changeEmailButton = findViewById(R.id.changeEmailButton);
//        ImageButton backButton = findViewById(R.id.backButton);
//        edtEmail = findViewById(R.id.emailEditText);
//        mAuth = FirebaseAuth.getInstance();
//        submitResetEmail = findViewById(R.id.submitResetEmail);
//        submitResetPassword = findViewById(R.id.submitResetPassword);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        newEmailEditText = findViewById(R.id.newEmailEditText);
//        submitResetEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ProfileActivity", "Submit button clicked");
//                String email = edtEmail.getText().toString().trim();
//                if (!TextUtils.isEmpty(email)) {
//                    mAuth.sendPasswordResetEmail(email)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(ProfileActivity.this, "Reset Password Link has been sent to your Email", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(ProfileActivity.this, "Failed to send reset password link: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                } else {
//                    edtEmail.setError("Email field can't be empty");
//                }
//            }
//        });

//        submitResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                String password = passwordEditText.getText().toString().trim();
//
//                if (!TextUtils.isEmpty(password)) {
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
//
//                    user.reauthenticate(credential)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                    String newEmail = newEmailEditText.getText().toString().trim();
//                                    user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    // Email address updated successfully
//                                                    Toast.makeText(ProfileActivity.this, "Email address updated successfully", Toast.LENGTH_SHORT).show();
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    // Handle the failure to update email address
//                                                    Toast.makeText(ProfileActivity.this, "Failed to update email address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(ProfileActivity.this, "Failed to reauthenticate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                } else {
//                    passwordEditText.setError("Password field can't be empty");
//                }
//            }


//        });


//        changePasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edtEmail.setVisibility(View.VISIBLE);
//                submitResetEmail.setVisibility(View.VISIBLE);
//            }
//        });
//
//        changeEmailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitResetPassword.setVisibility(View.VISIBLE);
//                passwordEditText.setVisibility(View.VISIBLE);
//                newEmailEditText.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//            }
//        });


    }
}