package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ReportActivity extends AppCompatActivity {

    LinearLayout homeView, scheduleView, notificationView, healthRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        homeView = findViewById(R.id.home2);
        scheduleView = findViewById(R.id.schedul3);
        notificationView = findViewById(R.id.notify2);
        healthRecord = findViewById(R.id.healthRecord);

        homeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, HomepageActivity.class));
            }
        });

        scheduleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, ScheduleActivity.class));
            }
        });

        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, NotificationsActivity.class));
            }
        });

        healthRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, HealthRecordActivity.class));
            }
        });
    }
}