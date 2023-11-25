package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        emailId = sharedPreferences.getString("email", "");

        if (emailId.endsWith("clinics.ca")) {
            patient.setVisibility(View.VISIBLE);
            setupDoctorProfile(emailId);
        } else {
            fullName.setText(sharedPreferences.getString("fullName", ""));
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

    private void setupDoctorProfile(String doctorEmail){

        // Reference to the "doctors" node in the Firebase database
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("doctors");
        Query query = databaseReference.orderByChild("email").equalTo(doctorEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        fullName.setText(userSnapshot.child("name").getValue(String.class));
                        email.setText(userSnapshot.child("specialty").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }
}