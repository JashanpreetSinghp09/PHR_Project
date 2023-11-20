package com.example.phr_application;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UploadLabResultActivity extends AppCompatActivity {

    private EditText editTextLabResult;
    private Button buttonUploadResult;
    private LabResultManager labResultManager;
    private String userPrivateKey = "0x10f122974834f05776d1e181a045231fc40991bc3e1536e977e31fe415bc42f7"; //actual private key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lab_result);

        editTextLabResult = findViewById(R.id.editTextLabResult);
        buttonUploadResult = findViewById(R.id.buttonUploadResult);

        labResultManager = new LabResultManager(userPrivateKey, "0xA6D2ca0953829aA3F4F8C40FcD0FeaF0a678fA09");

        buttonUploadResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String labResult = editTextLabResult.getText().toString();
                uploadLabResult(labResult);
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private void uploadLabResult(String labResult) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    return labResultManager.uploadLabResult(params[0]);
                } catch (Exception e) {
                    return "Upload failed: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                Toast.makeText(UploadLabResultActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }.execute(labResult);
    }


}
