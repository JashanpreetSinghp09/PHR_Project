package com.example.phr_application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.abi.FunctionEncoder;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.abi.datatypes.Function;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class WalletManager {

    private static WalletManager instance;
    private Web3j web3j;
    private Credentials credentials;
    private String walletDirectory;
    private String walletName;
    private String passwordString;
    private String address;
    private String balance;
    private String contractAddress;
    private static CustomGasProvider customGasProvider = new CustomGasProvider();

    private WalletManager(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("WalletPreferences", Context.MODE_PRIVATE);

        File externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (externalStorageDir != null) {
            walletDirectory = externalStorageDir.getAbsolutePath();
        } else {
            // Handle the case where external storage is not available or accessible
            Log.e("WalletManager", "External storage not available or accessible.");
            // You might want to use the internal storage as a fallback or take appropriate action.
        }

        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null || !provider.getClass().equals(BouncyCastleProvider.class)) {
            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
        }

        web3j = Web3j.build(new HttpService("https://sepolia.infura.io/v3/022a177facaa48488bff31b260295554"));
    }

    public static synchronized WalletManager getInstance(Context context) {
        if (instance == null) {
            instance = new WalletManager(context);
        }
        return instance;
    }

    public void generateWalletInBackground(String password, GenerateWalletCallback callback) {
        new GenerateWalletTask(callback).execute(password);
    }

    public void loadWalletCredentials(String password, String walletName, LoadWalletCallback callback) {
        new LoadWalletTask(callback).execute(password, walletName);
    }

    public interface GenerateWalletCallback {
        void onWalletGenerated(String walletName, String password, String message);

        void onWalletGenerationFailed(String errorMessage);
    }

    public interface LoadWalletCallback {
        void onWalletLoaded(String address, String balance, String message);

        void onWalletLoadFailed(String errorMessage);
    }

    private class GenerateWalletTask extends AsyncTask<String, Void, String> {
        private GenerateWalletCallback callback;

        GenerateWalletTask(GenerateWalletCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                passwordString = params[0];
                if (passwordString.isEmpty()) {
                    return "Please enter a password!";
                } else {
                    walletName = WalletUtils.generateNewWalletFile(passwordString, new File(walletDirectory));
                    return "Wallet generated successfully!";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (walletName != null) {
                callback.onWalletGenerated(walletName, passwordString, result);
            } else {
                callback.onWalletGenerationFailed(result);
            }
        }
    }

    private class LoadWalletTask extends AsyncTask<String, Void, String> {
        private LoadWalletCallback callback;
        private String errorMessage;

        LoadWalletTask(LoadWalletCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String password = params[0];
            String walletName = params[1];
            try {
                credentials = WalletUtils.loadCredentials(password, walletDirectory + "/" + walletName);
                address = credentials.getAddress();
                EthGetBalance ethGetBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
                balance = (Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER)).toString();
                return "Wallet loaded successfully!";
            } catch (IOException | ExecutionException | CipherException | InterruptedException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (address != null && balance != null) {
                callback.onWalletLoaded(address, balance, result);
            } else {
                callback.onWalletLoadFailed("Error: " + errorMessage);
            }
        }
    }

    public void deploy(DeployCallback callback) {
        new DeployContractTask(callback).execute();
    }

    public interface DeployCallback {
        void onDeploySuccess(String contractAddress);

        void onDeployFailed(String errorMessage);
    }

    @SuppressLint("StaticFieldLeak")
    private class DeployContractTask extends AsyncTask<Void, Void, String> {
        private DeployCallback callback;

        DeployContractTask(DeployCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                StringStorage contract = StringStorage.deploy(web3j, getTransactionManager(), customGasProvider).send();
                // Get the contract address
                contractAddress = contract.getContractAddress();
                return contractAddress;
            } catch (Exception e) {
                Log.e("error", "Error deploying contract: " + e.getMessage());
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.startsWith("Error")) {
                callback.onDeploySuccess(result);
            } else {
                callback.onDeployFailed(result);
            }
        }
    }

    public void uploadToSmartContract(String newString, UploadToSmartContractCallback callback) {
        new UploadStringTask(callback).execute(newString);
    }

    public void retrieveFromSmartContract(RetrieveFromSmartContractCallback callback) {
        new RetrieveStringTask(callback).execute();
    }

    public interface UploadToSmartContractCallback {
        void onUploadSuccess(String result);

        void onUploadFailed(String errorMessage);
    }

    public interface RetrieveFromSmartContractCallback {
        void onRetrieveSuccess(String result);

        void onRetrieveFailed(String errorMessage);
    }

    private class UploadStringTask extends AsyncTask<String, Void, String> {
        private UploadToSmartContractCallback callback;

        UploadStringTask(UploadToSmartContractCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String newString = params[0];
            try {
                // Load the existing contract
                TransactionManager transactionManager = getTransactionManager();
                StringStorage contract = StringStorage.load(contractAddress, web3j, transactionManager, new DefaultGasProvider());

                // Upload a new string
                uploadString(contract, newString);

                // Return success message
                return "String uploaded successfully";
            } catch (Exception e) {
                Log.e("error", "Error uploading string: " + e.getMessage());
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.startsWith("Error")) {
                callback.onUploadSuccess(result);
            } else {
                callback.onUploadFailed(result);
            }
        }
    }

    private class RetrieveStringTask extends AsyncTask<Void, Void, String> {
        private RetrieveFromSmartContractCallback callback;

        RetrieveStringTask(RetrieveFromSmartContractCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Load the existing contract
                TransactionManager transactionManager = getTransactionManager();
                StringStorage contract = StringStorage.load(contractAddress, web3j, transactionManager, new DefaultGasProvider());

                // Retrieve the stored string
                return retrieveString(contract);
            } catch (Exception e) {
                Log.e("error", "Error retrieving string: " + e.getMessage());
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.startsWith("Error")) {
                callback.onRetrieveSuccess(result);
            } else {
                callback.onRetrieveFailed(result);
            }
        }
    }

    private TransactionManager getTransactionManager() {
        if (credentials != null) {
            return new RawTransactionManager(web3j, credentials);
        } else {
            Log.e("error", "Credentials not available for transaction manager.");
            return null;
        }
    }

    private void uploadString(StringStorage contract, String newString) {
        try {
            // Create a function to call the uploadString method on the smart contract
            Function function = new Function(
                    "uploadString",
                    Arrays.asList(new org.web3j.abi.datatypes.Utf8String(newString)),
                    Arrays.asList()
            );

            // Encode the function and send the transaction
            String encodedFunction = FunctionEncoder.encode(function);
            contract.uploadString(encodedFunction).send();
        } catch (Exception e) {
            Log.e("error", "Error uploading string: " + e.getMessage());
        }
    }

    private String retrieveString(StringStorage contract) {
        try {
            // Call the retrieveString method on the smart contract
            String inputData = contract.retrieveString().send();
            return inputData;

        } catch (Exception e) {
            Log.e("error", "Error retrieving string: " + e.getMessage());
            return "";
        }
    }



    Credentials getCredentials() {

        return credentials;
    }

    Web3j getWeb3j(){

        return web3j;
    }
}
