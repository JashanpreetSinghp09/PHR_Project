package com.example.phr_application;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class ViewLabResultsActivity extends AppCompatActivity {

    private TextView textViewLabResults;
    private Button buttonViewResults;
    private LabResultManager labResultManager;
    private String userPrivateKey = "0x10f122974834f05776d1e181a045231fc40991bc3e1536e977e31fe415bc42f7"; // Replace with actual private key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lab_results);

        textViewLabResults = findViewById(R.id.textViewLabResults);
        buttonViewResults = findViewById(R.id.buttonViewResults);

        labResultManager = new LabResultManager(userPrivateKey, "0xA6D2ca0953829aA3F4F8C40FcD0FeaF0a678fA09");

        buttonViewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigInteger recordId = BigInteger.ZERO; // Replace with actual record ID as needed
                viewLabResults(recordId);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void viewLabResults(BigInteger recordId) {
        new AsyncTask<BigInteger, Void, String>() {

            @Override
            protected String doInBackground(BigInteger... params) {
                try {
                    return labResultManager.getLabResult(params[0]);
                } catch (Exception e) {
                    return "Error retrieving result: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                textViewLabResults.setText(result);
            }
        }.execute(recordId);
    }
}
