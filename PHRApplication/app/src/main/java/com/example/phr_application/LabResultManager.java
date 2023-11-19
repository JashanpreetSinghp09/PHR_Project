package com.example.phr_application;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class LabResultManager {

    private Web3j web3j;
    private Credentials credentials;
    private String contractAddress;

    public LabResultManager(String privateKey, String contractAddress) {
        this.web3j = Web3j.build(new HttpService("http://127.0.0.1:7545")); // Ganache URL
        this.credentials = Credentials.create(privateKey);
        this.contractAddress = contractAddress;
    }

    public String uploadLabResult(String labResult) throws Exception {
        Function function = new Function(
                "addRecord",
                Collections.singletonList(new Utf8String(labResult)),
                Collections.emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        EthSendTransaction transactionResponse = web3j.ethSendTransaction(
                        Transaction.createFunctionCallTransaction(
                                credentials.getAddress(),
                                null, // nonce
                                Transaction.DEFAULT_GAS, // gas price
                                BigInteger.valueOf(3000000), // gas limit
                                contractAddress,
                                encodedFunction))
                .send();

        if (transactionResponse.hasError()) {
            throw new Exception("Transaction failed: " + transactionResponse.getError().getMessage());
        }

        return transactionResponse.getTransactionHash();
    }

    public String getLabResult(BigInteger recordId) throws Exception {
        Function function = new Function(
                "getRecord",
                Collections.singletonList(new Uint256(recordId)),
                Collections.singletonList(new TypeReference<Utf8String>() {}));

        String encodedFunction = FunctionEncoder.encode(function);

        EthCall response = web3j.ethCall(
                        Transaction.createEthCallTransaction(credentials.getAddress(), contractAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST)
                .send();

        if (response.hasError()) {
            throw new Exception("Call failed: " + response.getError().getMessage());
        }

        List<Type> responseDecoded = FunctionReturnDecoder.decode(
                response.getValue(), function.getOutputParameters());

        if (!responseDecoded.isEmpty()) {
            return ((Utf8String) responseDecoded.get(0)).getValue();
        } else {
            throw new Exception("No value returned");
        }
    }
}
