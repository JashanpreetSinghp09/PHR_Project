package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    private Web3j web3j;

    private Credentials credentials;
    private String walletDirectory, walletName, password_string;

    private String storedEmail, storedPassword, storedWalletName;




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

        sharedPreferences = getSharedPreferences("WalletPreferences", MODE_PRIVATE);

        //Wallet Initialization
        storedEmail = sharedPreferences.getString("email", "");
        storedPassword = sharedPreferences.getString("password", "");
        storedWalletName = sharedPreferences.getString("walletName", "");

        if (!storedEmail.isEmpty() || !storedPassword.isEmpty()) {
            userEmail.setText(storedEmail);
            userPassword.setText(storedPassword);
        }

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

                    try {
                        credentials = WalletUtils.loadCredentials(storedPassword, walletDirectory + "/" + storedWalletName);
                        String address = credentials.getAddress();
                        EthGetBalance ethGetBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
                        String balance =  (Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER)).toString();

                        // Store address and balance in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("address", address);
                        editor.putString("balance", balance);
                        editor.apply();

                        Toast.makeText(MainActivity.this, address, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (CipherException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Redirecting to HomePage
                    startActivity(new Intent(MainActivity.this, HomepageActivity.class));

                }else {
                    Toast.makeText(MainActivity.this, "Login Failed!, Please check credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}