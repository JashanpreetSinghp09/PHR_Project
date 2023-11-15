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
    TextView textView_dr1, textView_spec1, textView_schedule1;
    TextView textView_dr2, textView_spec2, textView_schedule2;
    TextView textView_dr3, textView_spec3, textView_schedule3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        FirebaseApp.initializeApp(this);     // realtime DB firebase
        DatabaseReference databaseReference, databaseReferenceD2, databaseReferenceD3 = FirebaseDatabase.getInstance().getReference("doctors");

        homeView = findViewById(R.id.home);
        reportView = findViewById(R.id.report5);
        notificationView = findViewById(R.id.notify);




/*/
           --------------------Firebase DB update in schedule activity code begins------------------------------------------------
  */

        // doctor's schedule
        textView_dr1 = findViewById(R.id.textView_drName1);
        textView_spec1 = findViewById(R.id.textView_specialty1);
        textView_schedule1 = findViewById(R.id.textView18);

        textView_dr2 = findViewById(R.id.textView22);
        textView_spec2 = findViewById(R.id.textView23);
        textView_schedule2 = findViewById(R.id.textView21);

        textView_dr3 = findViewById(R.id.textView14);
        textView_spec3 = findViewById(R.id.textView15);
        textView_schedule3 = findViewById(R.id.textView13);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("doctors").child("d1");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String schedule = snapshot.child("schedule").getValue(String.class);
                    String specialty = snapshot.child("specialty").getValue(String.class);

                    // Now you can use the retrieved data as needed
                    textView_dr1.setText(name);
                    textView_spec1.setText(specialty);
                    textView_schedule1.setText(schedule);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });

        // Retrieve data for doctor "d2"
        databaseReferenceD2 = FirebaseDatabase.getInstance().getReference().child("doctors").child("d2");
        databaseReferenceD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String schedule = snapshot.child("schedule").getValue(String.class);
                    String specialty = snapshot.child("specialty").getValue(String.class);

                    // Now you can use the retrieved data as needed
                    textView_dr2.setText(name);
                    textView_spec2.setText(specialty);
                    textView_schedule2.setText(schedule);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });


        // Retrieve data for doctor "d3"
        databaseReferenceD3 = FirebaseDatabase.getInstance().getReference().child("doctors").child("d3");
        databaseReferenceD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String schedule = snapshot.child("schedule").getValue(String.class);
                    String specialty = snapshot.child("specialty").getValue(String.class);

                    // Now you can use the retrieved data as needed
                    textView_dr3.setText(name);
                    textView_spec3.setText(specialty);
                    textView_schedule3.setText(schedule);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });


/*/
        ----------------------------------Firebase DB update in schedule activity code ends--------------------------------------------------------------
 */


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

        // array of TextView IDs for dates and days

        int[] dateTextViewIds = {R.id.textView_date1, R.id.textView_date2, R.id.textView_date3, R.id.textView_date4, R.id.textView_date5, R.id.textView_date6, R.id.textView_date7, R.id.textView_date8, R.id.textView_date9, R.id.textView_date10};
        int[] dayTextViewIds = {R.id.textView_day1, R.id.textView_day2, R.id.textView_day3, R.id.textView_day4, R.id.textView_day5, R.id.textView_day6, R.id.textView_day7, R.id.textView_day8, R.id.textView_day9, R.id.textView_day10};


        // Get the current date and day
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());




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





    }

}