package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
    LinearLayout dr_appointment1, dr_appointment2,dr_appointment3;
    TextView textView_dr1, textView_spec1, textView_time1;
    TextView textView_dr2, textView_spec2, textView_time2;
    TextView textView_dr3, textView_spec3, textView_time3;
    LinearLayout[] linearLayoutArray = new LinearLayout[10];
    String targetDay;
    SharedPreferences sharedPreferences;


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

        //Setting up the schedules of doctors
        //Linear layout of each doctor
        dr1 = findViewById(R.id.dr1);
        dr2 = findViewById(R.id.dr2);
        dr3 = findViewById(R.id.dr3);

        dr_appointment1 = findViewById(R.id.appointments);
        dr_appointment2 = findViewById(R.id.appointments1);
        dr_appointment3 = findViewById(R.id.appointments2);

        // On Click to go to doctor details:
        dr_appointment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DoctorName", "d1");
                startActivity(new Intent(ScheduleActivity.this, DoctorDetailsActivity.class));
            }
        });

        dr_appointment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DoctorName", "d2");
                startActivity(new Intent(ScheduleActivity.this, DoctorDetailsActivity.class));
            }
        });

        dr_appointment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DoctorName", "d3");
                startActivity(new Intent(ScheduleActivity.this, DoctorDetailsActivity.class));
            }
        });

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

        // Array of doctor keys (d1, d2, d3)
        String[] doctorKeys = {"d1", "d2", "d3"};

        //Setting up the calender dates for schedules
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());

        //Used for referring to each linearLayout
        String linearLayouts = "linlayout";

        for(int i = 0; i < linearLayoutArray.length; i++){

            //Storing the reference of each date in the LinearLayout array
            linearLayoutArray[i] = findViewById(getResources().getIdentifier(linearLayouts + (i + 1), "id", getPackageName()));

            //Accessing the textView of each linearLayout for setting up the upcoming date and day
            for (int j = 0; j < 2; j++) {
                View childView = linearLayoutArray[i].getChildAt(j);

                if (j == 0) {
                    TextView date = (TextView) childView;
                    date.setText(dateFormat.format(calendar.getTime()));
                }

                else{
                    TextView day = (TextView) childView;
                    day.setText(dayOfWeekFormat.format(calendar.getTime()));
                    if(i == 0){

                        targetDay = dayOfWeekFormat.format(calendar.getTime());
                    }
                }
            }

            // increment the date for next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            //Setting up the onClick listener for each of the dates for updating schedule on click
            final int index = i;
            linearLayoutArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Resetting background color of the rest of the linear layouts
                    for (LinearLayout ll : linearLayoutArray) {
                        ll.setBackgroundResource(R.drawable.date1);
                    }

                    // Setting the background of the clicked LinearLayout
                    linearLayoutArray[index].setBackgroundResource(R.drawable.date);

                    //Setting up the schedule for that day
                    View childView = linearLayoutArray[index].getChildAt(1);
                    setupDoctorSchedule(doctorKeys, ((TextView) childView).getText().toString());
                }
            });
        }

        //Calling the schedule on create for current date
        setupDoctorSchedule(doctorKeys, targetDay);
    }

    /*
    Method used for updating schedules for a specific day
     */
    private void setupDoctorSchedule(String [] doctorKeys, String targetDay){

        // Reference to the "doctors" node in the Firebase database
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("doctors");

        //Setting linearlayout to gone
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