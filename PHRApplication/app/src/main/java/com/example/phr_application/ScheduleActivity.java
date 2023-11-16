package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    LinearLayout homeView, reportView, notificationView;
    LinearLayout dr1, dr2, dr3;
    TextView textView_dr1, textView_spec1, textView_time1;
    TextView textView_dr2, textView_spec2, textView_time2;
    TextView textView_dr3, textView_spec3, textView_time3;

    String targetDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        homeView = findViewById(R.id.home);
        reportView = findViewById(R.id.report5);
        notificationView = findViewById(R.id.notify);

        //Navbar setup
        homeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleActivity.this, HomepageActivity.class));
            }
        });

        reportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleActivity.this, ReportActivity.class));
            }
        });

        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleActivity.this, NotificationsActivity.class));
            }
        });


        //Setting up the upcoming dates and days
        // array of TextView IDs for dates and days
        int[] dateTextViewIds = {R.id.textView_date1, R.id.textView_date2, R.id.textView_date3, R.id.textView_date4, R.id.textView_date5, R.id.textView_date6, R.id.textView_date7, R.id.textView_date8, R.id.textView_date9, R.id.textView_date10};
        int[] dayTextViewIds = {R.id.textView_day1, R.id.textView_day2, R.id.textView_day3, R.id.textView_day4, R.id.textView_day5, R.id.textView_day6, R.id.textView_day7, R.id.textView_day8, R.id.textView_day9, R.id.textView_day10};

        // Get the current date and day
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        //targetDay = dayOfWeekFormat.format(Calendar.getInstance().getTime());
        targetDay = "Sat";

        //Changing date of schedule list
        for (int i = 0; i < dateTextViewIds.length; i++) {

            TextView textViewDate = findViewById(dateTextViewIds[i]);
            TextView textViewDay = findViewById(dayTextViewIds[i]);

            // Set the current date and day to the TextViews
            textViewDate.setText(dateFormat.format(calendar.getTime()));
            textViewDay.setText(dayOfWeekFormat.format(calendar.getTime()));

            // increment the date for next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }



        //Setting up the schedules of doctors
        //Linear layout of each doctor
        dr1 = findViewById(R.id.dr1);
        dr2 = findViewById(R.id.dr2);
        dr3 = findViewById(R.id.dr3);

        // text views related to the doctor's schedule
        textView_dr1 = findViewById(R.id.textView_drName1);
        textView_spec1 = findViewById(R.id.textView_specialty1);
        textView_time1 = findViewById(R.id.textView18);

        textView_dr2 = findViewById(R.id.textView22);
        textView_spec2 = findViewById(R.id.textView23);
        textView_time2 = findViewById(R.id.textView21);

        textView_dr3 = findViewById(R.id.textView14);
        textView_spec3 = findViewById(R.id.textView15);
        textView_time3 = findViewById(R.id.textView13);

        //Initializing the Firebase realtime database
        FirebaseApp.initializeApp(this);

        // Reference to the "doctors" node in the Firebase database
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("doctors");

        // Array of doctor keys (d1, d2, d3)
        String[] doctorKeys = {"d1", "d2", "d3"};

        //Setting linearlayout to invisible
        dr1.setVisibility(View.GONE);
        dr2.setVisibility(View.GONE);
        dr3.setVisibility(View.GONE);

        for (String doctorKey : doctorKeys) {
            databaseReference.child(doctorKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        String name = dataSnapshot.child("name").getValue(String.class);
                        String time = dataSnapshot.child("time").getValue(String.class);
                        String specialty = dataSnapshot.child("specialty").getValue(String.class);
                        DataSnapshot daysSnapshot = dataSnapshot.child("days");

                        if (daysSnapshot.exists()) {
                            Iterable<DataSnapshot> days = daysSnapshot.getChildren();

                            for (DataSnapshot day : days) {
                                String availableDay = day.getValue(String.class);

                                if (targetDay.equals(availableDay)) {
                                    if(doctorKey.equals("d1")){

                                        textView_dr1.setText(name);
                                        textView_spec1.setText(specialty);
                                        textView_time1.setText(time);
                                        dr1.setVisibility(View.VISIBLE);
                                    } else if (doctorKey.equals("d2")) {

                                        textView_dr2.setText(name);
                                        textView_spec2.setText(specialty);
                                        textView_time2.setText(time);
                                        dr2.setVisibility(View.VISIBLE);
                                    }  else if (doctorKey.equals("d3")) {

                                        textView_dr3.setText(name);
                                        textView_spec3.setText(specialty);
                                        textView_time3.setText(time);
                                        dr3.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                }
                            }
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
}