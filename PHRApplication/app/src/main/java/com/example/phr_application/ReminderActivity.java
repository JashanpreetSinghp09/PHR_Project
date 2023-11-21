package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;


public class ReminderActivity extends AppCompatActivity {


    EditText editTextMedicationName;
    Button buttonSetDateTime, buttonRemindMe;
    NotificationManager notificationManager;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        editTextMedicationName = findViewById(R.id.editTextMedicationName);
        buttonSetDateTime = findViewById(R.id.buttonSetDateTime);
        buttonRemindMe = findViewById(R.id.buttonRemindMe);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        buttonSetDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get medication name entered by the user
                String medicationName = editTextMedicationName.getText().toString();

                if (!medicationName.isEmpty()) {
                    // Get current date
                    Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);
                    int currentMonth = calendar.get(Calendar.MONTH);
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                    // Create DatePickerDialog to pick the date
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ReminderActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    selectedYear = year;
                                    selectedMonth = monthOfYear;
                                    selectedDay = dayOfMonth;

                                    // Create TimePickerDialog to pick the time
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    selectedHour = hourOfDay;
                                                    selectedMinute = minute;

                                                    // Calculate the delay for the notification
                                                    long delay = calculateDelayForNotification(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);

                                                    if (delay > 0) {
                                                        // Schedule the notification
                                                        Notification notification = getNotification("Take " + medicationName + " now!");
                                                        scheduleNotification(notification, delay);
                                                    } else {
                                                        // Handle invalid date or time selection
                                                        Toast.makeText(ReminderActivity.this, "Invalid Date/Time selected..", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                                    timePickerDialog.show();
                                }
                            }, currentYear, currentMonth, currentDay);
                    datePickerDialog.show();
                } else {
                    if(medicationName.isEmpty()){
                        Toast.makeText(ReminderActivity.this, "Please Enter Medication Name..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonRemindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private long calculateDelayForNotification(int year, int month, int day, int hour, int minute) {
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(year, month, day, hour, minute);

        // Get the current time in milliseconds
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        long selectedTimeInMillis = selectedCalendar.getTimeInMillis();

        // Calculate the delay between current time and selected time
        long delay = selectedTimeInMillis - currentTimeInMillis;

        // Ensure the delay is positive
        if (delay < 0) {
            //
            delay = 0;
        }

        return delay;
    }

    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                .setContentTitle("Medication Reminder")
                .setContentText(content)
                .setSmallIcon(R.drawable.reminder_btn)
                .setAutoCancel(true);

        return builder.build();
    }
}