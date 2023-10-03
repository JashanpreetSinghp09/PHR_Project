package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.*;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;   // for firebase authentication

    /*
    / edit View textview and button variables to store date and setting click listeners
     */
    private TextView existingUser;
    private EditText editText_userName, editText_userPassword, editText_confirm_userPassword, editText_email;
    private Button registerUser;
    private ProgressBar progressBar;
    private static final int LOG_IN_LINK_ID = R.id.logIn_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        /*
        / Initializing the variables
         */

        registerUser = findViewById(R.id.register_btn);
        existingUser = findViewById(R.id.logIn_link);

        registerUser.setOnClickListener(this);
        existingUser.setOnClickListener(this);

        editText_email = findViewById(R.id.email);
        editText_userName = findViewById(R.id.full_name);
        editText_userPassword = findViewById(R.id.password);
        editText_confirm_userPassword = findViewById(R.id.confirm_password);
        progressBar = findViewById(R.id.progressBar2);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == LOG_IN_LINK_ID) {
            startActivity(new Intent(this, MainActivity.class)); // Switching activity (intent)
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
        /*
        / progress bar is no visible after click and add user to FireBase using email and pass
         */

        progressBar.setVisibility(View.VISIBLE);

        /*/
        adding User object using constructor to fireBase database using email and password authentication in FireBase
         */

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            com.example.phr_application.User user = new com.example.phr_application.User(fullName, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                // re direct to log in page now !!

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to Register, Try Again", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to Register, Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}