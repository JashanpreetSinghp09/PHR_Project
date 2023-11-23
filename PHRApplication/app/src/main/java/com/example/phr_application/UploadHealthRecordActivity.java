package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;

public class UploadHealthRecordActivity extends AppCompatActivity {

    private EditText upload;
    private Button uploadButton;
    private WalletManager walletManager;

    SharedPreferences sharedPreferences;
    private String email, contractAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_health_record);

        upload = findViewById(R.id.uploadingRec);
        uploadButton = findViewById(R.id.uploadingButton);
        walletManager = WalletManager.getInstance(this);

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputData = upload.getText().toString().trim();

                //Retrieve contractAddress associated with the account from Firebase
                walletManager.retrieveContractAddress(email, new WalletManager.ContractAddressCallback() {

                    @Override
                    public void onUserNotFound() {

                        Toast.makeText(UploadHealthRecordActivity.this, "User not found for retrieving!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onContractAddressRetrieved(String contractAddress) {

                        //If there is no contract associated with the wallet, deploy a contract
                        if (contractAddress == null || contractAddress.isEmpty()){

                            //Deploy contract if there was no previous contract related to this local wallet
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

                                        System.out.println("Contract address is written to external storage.");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(UploadHealthRecordActivity.this, "Contract deployed successfully. Address: " + contractAddress, Toast.LENGTH_LONG).show();

                                    //Add the contract address to the users collection in Firebase
                                    uploadContractAddress(contractAddress);
                                }

                                //Prompting deployment failure
                                @Override
                                public void onDeployFailed(String errorMessage) {

                                    Toast.makeText(UploadHealthRecordActivity.this, "Failed to deploy contract. Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        //If there is a contract associated with the wallet
                        else{

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

                                System.out.println("Contract address is written to external storage.");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //Uploading the inputData to the contract
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

                            //Prompting on upload failure
                            @Override
                            public void onUploadFailed(String errorMessage) {
                                // Handle the upload failure
                                Toast.makeText(UploadHealthRecordActivity.this, "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    void uploadContractAddress(String contractAddress) {

        // Reference to the "users" collection
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");

        // Query to find the userKey based on the provided email
        Query query = usersReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the query found any matching user
                if (dataSnapshot.exists()) {
                    // Get the userKey
                    String userKey = dataSnapshot.getChildren().iterator().next().getKey();
                    usersReference.child(userKey).child("contractAddress").setValue(contractAddress);
                } else {
                    Toast.makeText(UploadHealthRecordActivity.this, "User not found for uploading!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error:", databaseError.getMessage());
            }
        });
    }
}