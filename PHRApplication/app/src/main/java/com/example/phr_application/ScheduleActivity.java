package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    LinearLayout homeView, reportView, notificationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        homeView = findViewById(R.id.home);
        reportView = findViewById(R.id.report5);
        notificationView = findViewById(R.id.notify);

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

        //array of TextView IDs for dates and days in appointment list

        int[] dateTextViewIds2 = {R.id.text_date1, R.id.text_date2, R.id.text_date3};
        int[] dayTextViewIds2 = {R.id.text_day1, R.id.text_day2, R.id.text_day3};



        // Get the current date and day
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());


        //Changing date of appointment ones
        for(int i = 0; i < dateTextViewIds2.length; i++){

            TextView textViewDate = findViewById(dateTextViewIds2[i]);
            TextView textViewDay = findViewById(dayTextViewIds2[i]);

            // Set the current date and day to the TextViews
            textViewDate.setText(dateFormat.format(calendar.getTime()));
            textViewDay.setText(dayOfWeekFormat.format(calendar.getTime()));
        }

        //Changing date of schedule list
        for(int i = 0; i < dateTextViewIds.length; i++){

            TextView textViewDate = findViewById(dateTextViewIds[i]);
            TextView textViewDay = findViewById(dayTextViewIds[i]);

            // Set the current date and day to the TextViews
            textViewDate.setText(dateFormat.format(calendar.getTime()));
            textViewDay.setText(dayOfWeekFormat.format(calendar.getTime()));

            // increment the date for next day
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
//        // Initialize the tv variable with tv objects
//
//
//        TextView textViewDate1 = findViewById(R.id.textView_date1);
//        TextView textViewDay1 = findViewById(R.id.textView_day1);
//        TextView textViewDate2 = findViewById(R.id.textView_date2);
//        TextView textViewDay2 = findViewById(R.id.textView_day2);
//
//        // Set the current date and day to the TextViews
//        textViewDate1.setText(dateFormat.format(calendar.getTime()));
//        textViewDay1.setText(dayOfWeekFormat.format(calendar.getTime()));
//
//        // increment the date for next day
//        calendar.add(Calendar.DAY_OF_MONTH,1);
//
//        // set the day and date for next day(s)
//        textViewDate2.setText(dateFormat.format(calendar.getTime()));
//        textViewDay2.setText(dayOfWeekFormat.format(calendar.getTime()));

    }
}