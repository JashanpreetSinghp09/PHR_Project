package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    TextView fullName, email;

    ImageView backButton;

    LinearLayout patient, payment, logout;

    String emailId;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.naming);
        email = findViewById(R.id.emailNaming);
        backButton = findViewById(R.id.imageView19);
        payment = findViewById(R.id.payment);
        patient = findViewById(R.id.patients);

        logout = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

        fullName.setText(sharedPreferences.getString("fullName", ""));

        emailId = sharedPreferences.getString("email", "");

        if (emailId.endsWith("clinics.ca")) {
            patient.setVisibility(View.VISIBLE);
            email.setText("Doctor");
        } else {
            patient.setVisibility(View.INVISIBLE);
            email.setText(emailId);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click
                finish();
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, PaymentActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sign-out from firebase
                mAuth.signOut();

                // Checking if the user has been successfully signed out
                if (mAuth.getCurrentUser() == null) {

                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                } else {

                    Toast.makeText(ProfileActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}