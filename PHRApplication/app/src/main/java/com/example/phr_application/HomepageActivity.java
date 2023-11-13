package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomepageActivity extends AppCompatActivity {

    TextView fullName;
    LinearLayout scheduleView, reportView, notificationView;

    ImageView profile;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        fullName = findViewById(R.id.textView2);
        scheduleView = findViewById(R.id.schedule);
        reportView = findViewById(R.id.report4);
        notificationView = findViewById(R.id.notification);
        profile = findViewById(R.id.imageView);

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

        fullName.setText(sharedPreferences.getString("fullName", ""));

        scheduleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, ScheduleActivity.class));
            }
        });

        reportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, ReportActivity.class));
            }
        });

        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, NotificationsActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, ProfileActivity.class));
            }
        });
    }
}