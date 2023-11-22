package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView register;
    private static final int signup_link= R.id.signup_link;

    // variables for log in of registered user ...
    private EditText userEmail, userPassword;
    private Button btn_Login;

    //Wallet declaration
    SharedPreferences sharedPreferences;

    private WalletManager walletManager;
    private String actualWalletName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);


        btn_Login = findViewById(R.id.login_button);
        btn_Login.setOnClickListener(this);

        register = findViewById(R.id.signup_link);   // for registration
        register.setOnClickListener(this);

        walletManager = WalletManager.getInstance(this);
        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

        String storedEmail = sharedPreferences.getString("email", "");
        String storedPassword = sharedPreferences.getString("password", "");


        if (!storedEmail.isEmpty() || !storedPassword.isEmpty()) {
            userEmail.setText(storedEmail);
            userPassword.setText(storedPassword);
        }
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

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

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

        walletManager.setWalletName(email, new WalletManager.WalletNameCallback() {
            @Override
            public void onUserNotFound() {

                // Handle the case when no user is found with the specified email
                Toast.makeText(MainActivity.this, "Wallet not found", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onWalletNameRetrieved(String fullName, String walletName) {


                SharedPreferences.Editor editor = sharedPreferences.edit();
                actualWalletName = walletName;
                editor.putString("fullName", fullName);
                editor.putString("walletName", walletName);
                editor.apply();
                Toast.makeText(MainActivity.this, walletName, Toast.LENGTH_LONG).show();
            }
        });

        /*/
        validating user login task using fire base authenticator
         */
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    walletManager.loadWalletCredentials(password, actualWalletName, new WalletManager.LoadWalletCallback(){

                        @Override
                        public void onWalletLoaded(String address, String balance, String message) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("address", address);
                            editor.putString("balance", balance);
                            editor.apply();

                            //Writing the local wallet address to the system
                            String fileName = "localWalletAddress.txt";

                            // Specify the content to be written to the file
                            String content = "address: " + address +"\nbalance: " + balance;

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

                            // Handle successful wallet loading
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            // Redirect to the login page
                            startActivity(new Intent(MainActivity.this, HomepageActivity.class));
                        }

                        @Override
                        public void onWalletLoadFailed(String errorMessage) {

                            // Handle wallet generation failure
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });

                }else {
                    Toast.makeText(MainActivity.this, "Login Failed!, Please check credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}