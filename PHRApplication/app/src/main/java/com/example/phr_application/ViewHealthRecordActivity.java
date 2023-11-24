package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewHealthRecordActivity extends AppCompatActivity {

    private TextView visiAllergy, visiCurrentMedication, visiImmunization, visiCurrentSymptoms, visiDiagnosis, visiTreatmentPlan;
    private TextView allergy, currentMedication, immunization, currentSymptoms, diagnosis, treatmentPlan;
    private Button viewButton;
    private WalletManager walletManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_health_record);

        visiAllergy = findViewById(R.id.textView16);
        visiCurrentMedication = findViewById(R.id.textView28);
        visiImmunization = findViewById(R.id.textView30);
        visiCurrentSymptoms = findViewById(R.id.textView32);
        visiDiagnosis = findViewById(R.id.textView34);
        visiTreatmentPlan = findViewById(R.id.textView36);

        visiAllergy.setVisibility(View.INVISIBLE);
        visiCurrentMedication.setVisibility(View.INVISIBLE);
        visiImmunization.setVisibility(View.INVISIBLE);
        visiCurrentSymptoms.setVisibility(View.INVISIBLE);
        visiDiagnosis.setVisibility(View.INVISIBLE);
        visiTreatmentPlan.setVisibility(View.INVISIBLE);


        allergy = findViewById(R.id.viewingAllergy);
        currentMedication = findViewById(R.id.viewingMedication);
        immunization = findViewById(R.id.viewingImmunization);
        currentSymptoms = findViewById(R.id.viewingSymptoms);
        diagnosis = findViewById(R.id.viewingDiagnosis);
        treatmentPlan = findViewById(R.id.viewingTreatment);


        viewButton = findViewById(R.id.viewingButton);

        walletManager = WalletManager.getInstance(this);

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                walletManager.retrieveFromSmartContract(new WalletManager.RetrieveFromSmartContractCallback() {
                    @Override
                    public void onRetrieveSuccess(String result) {

                        setInputData(result, allergy, currentMedication, immunization, currentSymptoms, diagnosis, treatmentPlan);
                        setVisibility(visiAllergy, visiCurrentMedication, visiImmunization, visiCurrentSymptoms, visiDiagnosis, visiTreatmentPlan);
                    }

                    @Override
                    public void onRetrieveFailed(String errorMessage) {

                        Toast.makeText(ViewHealthRecordActivity.this, "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void setInputData(String inputData, TextView... textViews) {

        String[] substrings = inputData.split("\n");

        // Iterate over the textViews and set the text for each one
        for (int i = 0; i < Math.min(substrings.length, textViews.length); i++) {
            textViews[i].setText(substrings[i]);
        }
    }

    void setVisibility(TextView... textViews) {

        for (TextView textView : textViews) {
            textView.setVisibility(View.VISIBLE);
        }
    }
}