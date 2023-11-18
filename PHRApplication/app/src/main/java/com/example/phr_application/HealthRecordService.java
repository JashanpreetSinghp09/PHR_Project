package com.example.phr_application;
/*
import com.example.phr_application.contracts.HealthRecord;
import com.example.phr_application.contracts.HealthRecordContract;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class HealthRecordService {
    private final Web3j web3j;
    private final ContractGasProvider contractGasProvider;
    private final HealthRecord healthRecord;

    public HealthRecordService(Web3j web3j, ContractGasProvider contractGasProvider, String contractAddress, String privateKey) {
        this.web3j = web3j;
        this.contractGasProvider = contractGasProvider;

        // Load the contract instance
        Credentials credentials = Credentials.create(privateKey);
        healthRecord = HealthRecord.load(contractAddress, web3j, credentials, contractGasProvider);
    }

    public String addRecord(String data) throws Exception {
        // Call the addRecord method of the smart contract
        return healthRecord.addRecord(data).send().getTransactionHash();
    }

    public String getRecord(int recordId) throws Exception {
        // Call the getRecord method of the smart contract
        return healthRecord.getRecord(BigInteger.valueOf(recordId)).send();
    }
}
*/