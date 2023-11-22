package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class UploadHealthRecordActivity extends AppCompatActivity {

    private EditText upload;
    private Button uploadButton;
    private WalletManager walletManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_health_record);

        upload = findViewById(R.id.uploadingRec);
        uploadButton = findViewById(R.id.uploadingButton);
        walletManager = WalletManager.getInstance(this);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputData = upload.getText().toString().trim();
                walletManager.deploy(new WalletManager.DeployCallback() {
                    @Override
                    public void onDeploySuccess(String contractAddress) {

                        //Writing the contract address to the system
                        String fileName = "smartContractAddress.txt";

                        // Specify the content to be written to the file
                        String content = "address: " + contractAddress;

                        // Get the public external storage directory
                        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

                        // Create the file object
                        File file = new File(directory, fileName);

                        // Try to create the file and write the content
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            // Convert the string content to bytes
                            byte[] contentBytes = content.getBytes();

                            // Write the bytes to the file
                            fos.write(contentBytes);

                            System.out.println("File created and content written to external storage.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(UploadHealthRecordActivity.this, "Contract deployed successfully. Address: " + contractAddress, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDeployFailed(String errorMessage) {

                        Toast.makeText(UploadHealthRecordActivity.this, "Failed to deploy contract. Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

                walletManager.uploadToSmartContract(inputData, new WalletManager.UploadToSmartContractCallback() {
                    @Override
                    public void onUploadSuccess(String result) {

                        //Writing the uploaded data to the system
                        String fileName = "inputData.txt";

                        // Get the public external storage directory
                        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

                        // Create the file object
                        File file = new File(directory, fileName);

                        // Try to create the file and write the content
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            // Convert the string content to bytes
                            byte[] contentBytes = inputData.getBytes();

                            // Write the bytes to the file
                            fos.write(contentBytes);

                            System.out.println("File created and content written to external storage.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Handle the successful upload
                        Toast.makeText(UploadHealthRecordActivity.this, "Success: " + result, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UploadHealthRecordActivity.this, ViewHealthRecordActivity.class));
                    }
                    @Override
                    public void onUploadFailed(String errorMessage) {
                        // Handle the upload failure
                        Toast.makeText(UploadHealthRecordActivity.this, "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}