package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.*;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;   // for firebase authentication

    /*
    / edit View textview and button variables to store date and setting click listeners
     */
    private TextView existingUser;
    private EditText editText_userName, editText_userPassword, editText_confirm_userPassword, editText_email;
    private Button registerUser;


    //Wallet Declaration
    SharedPreferences sharedPreferences;
    private WalletManager walletManager;

    private static final int LOG_IN_LINK_ID = R.id.logIn_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerUser = findViewById(R.id.register_btn);
        existingUser = findViewById(R.id.logIn_link);

        registerUser.setOnClickListener(this);
        existingUser.setOnClickListener(this);

        editText_email = findViewById(R.id.email);
        editText_userName = findViewById(R.id.full_name);
        editText_userPassword = findViewById(R.id.password);
        editText_confirm_userPassword = findViewById(R.id.confirm_password);

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);
        walletManager = WalletManager.getInstance(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == LOG_IN_LINK_ID) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        } else if (viewId == R.id.register_btn) {
            registerUser(); // Function to handle user registration
        }
    }

    private void registerUser() {
        String email= editText_email.getText().toString().trim();
        String fullName = editText_userName.getText().toString().trim();
        String password = editText_userPassword.getText().toString().trim();
        String confirmed_password = editText_confirm_userPassword.getText().toString().trim();

        /*

        / making sure all the information is added correctly!
         */
        if(fullName.isEmpty()){
            editText_userName.setError("Full Name is required!!");
            editText_userName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editText_email.setError("E-mail is required!!");
            editText_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText_email.setError("Invalid Email!!");
            editText_email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editText_userPassword.setError("Password is required!!");
            editText_userPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editText_userPassword.setError("Password too short!!");
            editText_userPassword.requestFocus();
            return;
        }

        if(!password.equals(confirmed_password)){
            editText_confirm_userPassword.setError("Passwords do not match!!");
            editText_confirm_userPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            walletManager.generateWalletInBackground(password, new WalletManager.GenerateWalletCallback() {

                                @Override
                                public void onWalletGenerated(String walletName, String password, String message) {

                                    sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("walletName", walletName);
                                    editor.putString("password", password);
                                    editor.putString("email", email);
                                    editor.putString("fullName", fullName);
                                    editor.apply();
                                    storeUserDataInFirebase(email, walletName);

                                    // Handle successful wallet generation
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                    // Redirect to the login page
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }

                                @Override
                                public void onWalletGenerationFailed(String errorMessage) {

                                    // Handle wallet generation failure
                                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to Register, Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void storeUserDataInFirebase(String email, String walletName) {
        // Generating a unique key for the user in the "users" collection

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
        String userKey = usersReference.push().getKey();

        User user = new User(email, walletName);

        // Storing email as key and email as value
        usersReference.child(userKey).child("email").setValue(email);

        // Storing walletName as key and walletName as value
        usersReference.child(userKey).child("walletName").setValue(walletName);
    }
}