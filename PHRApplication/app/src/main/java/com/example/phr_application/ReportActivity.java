package com.example.phr_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    LinearLayout homeView, scheduleView, notificationView, labResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        homeView = findViewById(R.id.home2);
        scheduleView = findViewById(R.id.schedul3);
        notificationView = findViewById(R.id.notify2);
        labResultView = findViewById(R.id.LabResult); // Make sure this ID matches in your XML

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

        // Set OnClickListener for the Lab Results LinearLayout
        labResultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to start the UploadLabResultActivity
                startActivity(new Intent(ReportActivity.this, UploadLabResultActivity.class));
            }
        });
    }
}
