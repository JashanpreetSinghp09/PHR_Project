package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView register;
    private static final int signup_link= R.id.signup_link;

    // variables for log in of registered user ...
    private EditText userEmail, userPassword;
    private Button btn_Login;
    // -------------------//
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        btn_Login = findViewById(R.id.login_button);
        btn_Login.setOnClickListener(this);

        register = findViewById(R.id.signup_link);   // for registration
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == signup_link) {
            startActivity(new Intent(this, RegisterActivity.class)); // Switching activity (intent)
        } else if (viewId == R.id.login_button) {
            userLogin(); // Function to handle user registration
        }
    }

    private void userLogin() {

        /*/
        ensuring correct information is added for log in
         */
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if(email.isEmpty()){
            userEmail.setError("Please enter e-mail");
            userEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Please enter valid e-mail");
            userEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            userPassword.setError("Please enter password");
            userPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            userPassword.setError("minimum password length is 6 characters");
            userPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        /*/
        validating user login task using fire base authenticator
         */
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // redirect to App Home Page!! (poll part!)      // creating empty activity for now!!
                    startActivity(new Intent(MainActivity.this, HomepageActivity.class));
                    //Toast.makeText(MainActivity.this, "Login  success", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(MainActivity.this, "Login Failed!, Please check credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}