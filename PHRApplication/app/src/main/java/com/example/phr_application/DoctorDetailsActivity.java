package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

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


        String storedDr = sharedPreferences.getString("DoctorName", "dr1");

    }
}