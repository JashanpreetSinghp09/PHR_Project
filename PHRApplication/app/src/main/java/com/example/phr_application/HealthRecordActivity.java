package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class HealthRecordActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Button uploadHealthRecord, viewHealthRecord;
    private WalletManager walletManager;
    private String email, contractAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);

        uploadHealthRecord = findViewById(R.id.uploadHealth);
        viewHealthRecord = findViewById(R.id.viewHealth);

        walletManager = WalletManager.getInstance(this);

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");

        uploadHealthRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HealthRecordActivity.this, UploadHealthRecordActivity.class));
            }
        });

        viewHealthRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If contractAddress exists in Firebase, let user view their health record
                walletManager.retrieveContractAddress(email, new WalletManager.ContractAddressCallback() {

                    @Override
                    public void onUserNotFound() {

                        Toast.makeText(HealthRecordActivity.this, "User not found!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onContractAddressRetrieved(String contractAddress) {

                        //If there is no contract associated with the wallet, deploy a contract
                        if (contractAddress == null || contractAddress.isEmpty()){

                            Toast.makeText(HealthRecordActivity.this, "Please upload a health record!", Toast.LENGTH_LONG).show();
                        }

                        //If there is a contract associated with the wallet
                        else{

                            startActivity(new Intent(HealthRecordActivity.this, ViewHealthRecordActivity.class));
                        }
                    }
                });
            }
        });
    }
}