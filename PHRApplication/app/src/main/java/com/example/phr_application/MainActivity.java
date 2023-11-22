package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.util.concurrent.ExecutionException;

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




        /*/
        validating user login task using fire base authenticator
         */
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String storedPassword = sharedPreferences.getString("password", "");
                    String storedWalletName = sharedPreferences.getString("walletName", "");

                    if (!storedWalletName.isEmpty()) {
                        walletManager.loadWalletCredentials(storedPassword, storedWalletName, new WalletManager.LoadWalletCallback(){

                            @Override
                            public void onWalletLoaded(String address, String balance, String message) {

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("address", address);
                                editor.putString("balance", balance);
                                editor.apply();

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
                    }


                }else {
                    Toast.makeText(MainActivity.this, "Login Failed!, Please check credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}