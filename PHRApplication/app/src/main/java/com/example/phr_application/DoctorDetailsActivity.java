package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    TextView drName, drSpecialty, drAboutSection, drAvailability;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        drName = findViewById(R.id.tv_docName);
        drSpecialty = findViewById(R.id.tv_docRole);
        drAboutSection = findViewById(R.id.tv_about);
        drAvailability = findViewById(R.id.tv_time);

        sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);
        System.out.println(sharedPreferences.getString("DoctorName", ""));
        //Initializing the Firebase realtime database
        FirebaseApp.initializeApp(DoctorDetailsActivity.this);
//        setupDoctorDetails(sharedPreferences.getString("DoctorName", ""));


    }

    private void setupDoctorDetails(String doctorKey){

        // Reference to the "doctors" node in the Firebase database
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("doctors");

        databaseReference.child(doctorKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    drName.setText(dataSnapshot.child("name").getValue(String.class));
                    String name = dataSnapshot.child("name").getValue(String.class);
//                    Toast.makeText(DoctorDetailsActivity.this, name, Toast.LENGTH_LONG).show();
//                    System.out.println(name);
                    drSpecialty.setText(dataSnapshot.child("specialty").getValue(String.class));
                    drAvailability.setText(dataSnapshot.child("time").getValue(String.class));
                    drAboutSection.setText(dataSnapshot.child("about").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });

    }
}