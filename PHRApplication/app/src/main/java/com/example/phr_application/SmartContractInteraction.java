package com.example.phr_application;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.phr_application.StringStorage;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SmartContractInteraction {

    private static final String TAG = "SmartContractInteraction";
    private static final String INFURA_URL = "https://sepolia.infura.io/v3/022a177facaa48488bff31b260295554";

    private static String walletDirectory;
    private static String walletName;

    private static String address;

    private static String balance;

    private static String contractAddress;
    private static final String passwordString = "farhan";

    private static Credentials credentials;
    private static Context context;

    // Set gas price (100 Gwei)
    static BigInteger gasPriceGwei = BigInteger.valueOf(100);
    static BigInteger gasPriceWei = gasPriceGwei.multiply(DefaultGasProvider.GAS_PRICE);

    // Set gas limit (21000)
    static BigInteger gasLimit = BigInteger.valueOf(100000);

    public static CompletableFuture<Void> upload2Async(Context context, Web3j web3j, Credentials credentials) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String result = "";

                // Deploying the contract
                StringStorage deploy = deployContract(web3j, credentials);
                showToast(context, "Deploy Success!");
                System.out.println("Deploy Success!");
                contractAddress = deploy.getContractAddress();

                // Create a transaction manager
                TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

                // Load the existing contract
                StringStorage contract = StringStorage.load(contractAddress, web3j, transactionManager, new DefaultGasProvider());

                // Upload a new string
                uploadString(contract, "hello block");
                showToast(context, "Upload Success!");
                System.out.println("Upload Success!");

                // Retrieve the stored string
                result = retrieveString(contract);
                showToast(context, result);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private static void showToast(Context context, String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }


    public static void upload(Context context){

        try {

            final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
            if (provider == null || !provider.getClass().equals(BouncyCastleProvider.class)) {
                Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
                Security.insertProviderAt(new BouncyCastleProvider(), 1);
            }

            // Connect to the Ethereum node through Infura
            Web3j web3j = Web3j.build(new HttpService(INFURA_URL));

            File externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (externalStorageDir != null) {
                walletDirectory = externalStorageDir.getAbsolutePath();
            } else {
                // Handle the case where external storage is not available or accessible
                Log.e("WalletManager", "External storage not available or accessible.");
                // You might want to use the internal storage as a fallback or take appropriate action.
            }

            try {
                walletName = WalletUtils.generateNewWalletFile(passwordString, new File(walletDirectory));
                Toast.makeText(context, "Wallet generation Success!", Toast.LENGTH_SHORT).show();
                System.out.println("Wallet generation Success!");
            } catch (Exception e) {
                e.printStackTrace();
            }

            credentials = WalletUtils.loadCredentials(passwordString, walletDirectory + "/" + walletName);
            Toast.makeText(context, "Wallet loading Success!", Toast.LENGTH_SHORT).show();
            System.out.println("Wallet loading Success!");

            address = credentials.getAddress();
            EthGetBalance ethGetBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
            balance = (Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER)).toString();
        } catch (Exception e) {
            System.out.println("Error interacting with smart contract: " + e.getMessage());
        }
    }

    private static StringStorage deployContract(Web3j web3j, Credentials credentials) throws Exception {
        RemoteCall<StringStorage> deployCall = StringStorage.deploy(web3j, credentials, gasPriceWei, gasLimit);
        return deployCall.send();
    }

    private static void uploadString(StringStorage contract, String newString) throws Exception {
        contract.uploadString(newString).send();
    }

    private static String retrieveString(StringStorage contract) throws Exception {
        String retrievedString = contract.retrieveString().send();
        return retrievedString;
    }
}
