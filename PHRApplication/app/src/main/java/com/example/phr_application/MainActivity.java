package com.example.phr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
//import com.example.phr_application.MyContract;

import java.io.IOException;
import java.math.BigInteger;

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






        /*Web3j web3 = Web3j.build(new HttpService("https://sepolia.infura.io/v3/022a177facaa48488bff31b260295554"));
        try {
            Credentials credentials = WalletUtils.loadCredentials("Ge$haan226975", "0xb30bAacD5B0CA63Bc49ac35EC8108f0073BCb60e");
            String contractABI = "[{\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"x\",\"type\":\"uint256\"}],\"name\":\"set\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";
            String contractBinary = "608060405234801561000f575f80fd5b506101438061001d5f395ff3fe608060405234801561000f575f80fd5b5060043610610034575f3560e01c806360fe47b1146100385780636d4ce63c14610054575b5f80fd5b610052600480360381019061004d91906100ba565b610072565b005b61005c61007b565b60405161006991906100f4565b60405180910390f35b805f8190555050565b5f8054905090565b5f80fd5b5f819050919050565b61009981610087565b81146100a3575f80fd5b50565b5f813590506100b481610090565b92915050565b5f602082840312156100cf576100ce610083565b5b5f6100dc848285016100a6565b91505092915050565b6100ee81610087565b82525050565b5f6020820190506101075f8301846100e5565b9291505056fea2646970667358221220e7340f11a55e68397a0a3e2f2f0721bd7ae10c10c3c88885c9c0aa1764609f1464736f6c63430008150033";
            BigInteger gasPrice = new BigInteger("20000000000"); // Gas price
            BigInteger gasLimit = new BigInteger("4712388"); // Gas limit

            TransactionManager transactionManager = new RawTransactionManager(web3, credentials);
//
//            MyContract contract = YourSmartContract.deploy(web3, transactionManager, gasPrice, gasLimit, contractBinary, "constructor_params").send();
//
//            String contractAddress = contract.getContractAddress();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }*/



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
                    startActivity(new Intent(MainActivity.this, WalletLoginActivity.class));
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