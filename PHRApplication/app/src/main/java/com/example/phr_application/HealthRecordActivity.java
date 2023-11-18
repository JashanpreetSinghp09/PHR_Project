package com.example.phr_application;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordActivity extends AppCompatActivity {

    private static final String CONTRACT_ADDRESS = "0xA6D2ca0953829aA3F4F8C40FcD0FeaF0a678fA09";
    private static final String PRIVATE_KEY = "0x10f122974834f05776d1e181a045231fc40991bc3e1536e977e31fe415bc42f7";

    private Web3j web3j;
    private Credentials credentials;

    private BigInteger gasPrice;
    private BigInteger gasLimit;

    private HealthRecordContract healthRecordContract;

    private EditText diagnosisEditText, treatmentsEditText, medicationsEditText,
            allergiesEditText, immunizationsEditText, labResultsEditText;
    private Button updateRecordButton, retrieveRecordButton, uploadFileButton;
    private RecyclerView healthReportsRecyclerView;
    private HealthReportAdapter healthReportAdapter;
    private List<String> healthReportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);

        web3j = Web3j.build(new HttpService("http://127.0.0.1:7545\n"));
        credentials = Credentials.create(PRIVATE_KEY);
        healthRecordContract = HealthRecordContract.load(CONTRACT_ADDRESS, web3j, credentials, gasPrice, gasLimit);

        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        treatmentsEditText = findViewById(R.id.treatmentsEditText);
        medicationsEditText = findViewById(R.id.medicationsEditText);
        allergiesEditText = findViewById(R.id.allergiesEditText);
        immunizationsEditText = findViewById(R.id.immunizationsEditText);
        labResultsEditText = findViewById(R.id.labResultsEditText);

        updateRecordButton = findViewById(R.id.updateRecordButton);
        retrieveRecordButton = findViewById(R.id.retrieveRecordButton);
       // uploadFileButton = findViewById(R.id.uploadFileButton);
       // healthReportsRecyclerView = findViewById(R.id.healthReportsRecyclerView);

        // Initialize RecyclerView
        healthReportsList = new ArrayList<>();
        healthReportAdapter = new HealthReportAdapter(healthReportsList);
        healthReportsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        healthReportsRecyclerView.setAdapter(healthReportAdapter);

        updateRecordButton.setOnClickListener(v -> updateHealthRecord());
        retrieveRecordButton.setOnClickListener(v -> retrieveHealthRecord());
        uploadFileButton.setOnClickListener(v -> uploadFile());
    }

    private void updateHealthRecord() {
        String diagnosis = diagnosisEditText.getText().toString();
        String treatments = treatmentsEditText.getText().toString();
        String medications = medicationsEditText.getText().toString();
        String allergies = allergiesEditText.getText().toString();
        String immunizations = immunizationsEditText.getText().toString();
        String labResults = labResultsEditText.getText().toString();

        new UpdateHealthRecordTask().execute(diagnosis, treatments, medications, allergies, immunizations, labResults);
    }

    private void retrieveHealthRecord() {
        new RetrieveHealthRecordTask().execute();
    }

    private void uploadFile() {
        // Handle the logic for uploading files
        // This can involve interacting with the smart contract or a separate file storage system
        // Update the healthReportsList accordingly
        healthReportsList.add("New File"); // Replace this with the actual file information
        healthReportAdapter.notifyDataSetChanged();
    }

    private class UpdateHealthRecordTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String diagnosis = params[0];
                String treatments = params[1];
                String medications = params[2];
                String allergies = params[3];
                String immunizations = params[4];
                String labResults = params[5];

                Tuple7<String, String, String, String, String, String, String> healthRecord = healthRecordContract
                        .updateHealthRecord(diagnosis, treatments, medications, allergies, immunizations, labResults)
                        .send();

                // Assuming the updateHealthRecord function returns the updated health record
                return healthRecord.toString();
            } catch (Exception e) {
                Log.e("UpdateHealthRecordTask", "Error updating health record", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(HealthRecordActivity.this, "Health record updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HealthRecordActivity.this, "Failed to update health record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RetrieveHealthRecordTask extends AsyncTask<Void, Void, Tuple7<String, String, String, String, String, String, String>> {
        @Override
        protected Tuple7<String, String, String, String, String, String, String> doInBackground(Void... params) {
            try {
                return healthRecordContract.getHealthRecord(credentials.getAddress()).send();
            } catch (Exception e) {
                Log.e("RetrieveHealthRecordTask", "Error retrieving health record", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Tuple7<String, String, String, String, String, String, String> healthRecord) {
            if (healthRecord != null) {
                displayHealthRecord(healthRecord);
            } else {
                Toast.makeText(HealthRecordActivity.this, "Failed to retrieve health record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayHealthRecord(Tuple7<String, String, String, String, String, String, String> healthRecord) {
        // Display the retrieved health record in your UI components (TextViews, etc.)
        // Example:
        diagnosisEditText.setText(healthRecord.component1());
        treatmentsEditText.setText(healthRecord.component2());
        medicationsEditText.setText(healthRecord.component3());
        allergiesEditText.setText(healthRecord.component4());
        immunizationsEditText.setText(healthRecord.component5());
        labResultsEditText.setText(healthRecord.component6());
    }
}
