package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.*;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.security.Provider;
import java.security.Security;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;   // for firebase authentication

    /*
    / edit View textview and button variables to store date and setting click listeners
     */
    private TextView existingUser;
    private EditText editText_userName, editText_userPassword, editText_confirm_userPassword, editText_email;
    private Button registerUser;


    //Wallet Declaration
    SharedPreferences sharedPreferences;
    private Web3j web3j;
    private String walletDirectory, walletName, password_string;

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


        //Wallet Initializations
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {

            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {

            return;
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        walletDirectory = getFilesDir().getAbsolutePath();

        web3j = Web3j.build(new HttpService("https://sepolia.infura.io/v3/022a177facaa48488bff31b260295554"));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();
            if (!clientVersion.hasError()) {
                Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
            } else {
                Log.d("error", clientVersion.getError().getMessage());
                Toast.makeText(this, clientVersion.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    class GenerateWalletTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                password_string = editText_userPassword.getText().toString().trim();
                if (password_string.isEmpty()) {
                    return "Please enter a password!";
                } else {
                    // Generate the wallet in the background
                    walletName = WalletUtils.generateNewWalletFile(password_string, new File(walletDirectory));
                    return "Wallet generated successfully!";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG).show();
            if (walletName != null) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("walletName", walletName);
                editor.putString("password", password_string);
                editor.apply();
                Toast.makeText(RegisterActivity.this, walletName, Toast.LENGTH_LONG).show();

                //Redirecting to the login page
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
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


        /*/
        adding User object using constructor to fireBase database using email and password authentication in FireBase
         */

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            com.example.phr_application.User user = new com.example.phr_application.User(fullName, email);
                            Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();

                            // Save the email to SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.apply();

                            new GenerateWalletTask().execute();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to Register, Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}