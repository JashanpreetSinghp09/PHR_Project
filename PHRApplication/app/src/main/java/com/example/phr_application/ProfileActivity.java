package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    TextView fullName, email;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.naming);
        email = findViewById(R.id.emailNaming);
        backButton = findViewById(R.id.imageView19);

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

        fullName.setText(sharedPreferences.getString("fullName", ""));
        email.setText(sharedPreferences.getString("email", ""));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click
                finish();
            }
        });
    }
}