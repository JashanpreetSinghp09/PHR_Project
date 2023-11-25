package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView drName, drSpecialty, drAboutSection, drAvailability, drPatientNo, drExperience, drRating;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        drName = findViewById(R.id.tv_docName);
        drSpecialty = findViewById(R.id.tv_docRole);
        drAboutSection = findViewById(R.id.tv_about);
        drAvailability = findViewById(R.id.tv_time);
        drPatientNo = findViewById(R.id.tv_noOfPatients);
        drExperience = findViewById(R.id.tv_experience);
        drRating = findViewById(R.id.tv_rating);
        backButton = findViewById(R.id.imageView_back);

        //Initializing the Firebase realtime database
        FirebaseApp.initializeApp(DoctorDetailsActivity.this);

        //Taking doctor identity
        sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);

        //Setting doctor details
        setupDoctorDetails(sharedPreferences.getString("DoctorName", ""));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click
                finish();
            }
        });


    }

    private void setupDoctorDetails(String doctorKey){

        // Reference to the "doctors" node in the Firebase database
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("doctors");

        databaseReference.child(doctorKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    drName.setText(dataSnapshot.child("name").getValue(String.class));
                    drSpecialty.setText(dataSnapshot.child("specialty").getValue(String.class));
                    drAvailability.setText(dataSnapshot.child("time").getValue(String.class));
                    drAboutSection.setText(dataSnapshot.child("about").getValue(String.class));
                    drPatientNo.setText(dataSnapshot.child("noOfPatients").getValue(String.class));
                    drExperience.setText(dataSnapshot.child("experience").getValue(String.class));
                    drRating.setText(dataSnapshot.child("rating").getValue(Double.class).toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });

    }
}