package com.example.phr_application;

import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class CustomGasProvider implements ContractGasProvider {



    static BigInteger gasPriceGwei = BigInteger.valueOf(1);
    static BigInteger gasPriceWei = gasPriceGwei.multiply(DefaultGasProvider.GAS_PRICE);

    private final BigInteger gasPrice = gasPriceWei;
    private final BigInteger gasLimit = BigInteger.valueOf(1000000);

    @Override
    public BigInteger getGasPrice(String contractFunc) {
        return gasPrice;
    }

    @Override
    public BigInteger getGasPrice() {
        return gasPrice;
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return gasLimit;
    }

    @Override
    public BigInteger getGasLimit() {
        return gasLimit;
    }
}
