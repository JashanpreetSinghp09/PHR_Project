package com.example.phr_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
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

    private WalletManager(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("WalletPreferences", Context.MODE_PRIVATE);
        walletDirectory = context.getFilesDir().getAbsolutePath();

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
}
