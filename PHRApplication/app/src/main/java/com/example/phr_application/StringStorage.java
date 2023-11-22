package com.example.phr_application;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.0.
 */
@SuppressWarnings("rawtypes")
public class StringStorage extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b506106808061001d5f395ff3fe608060405234801561000f575f80fd5b5060043610610034575f3560e01c80632e8e4c7814610038578063516a431b14610056575b5f80fd5b610040610072565b60405161004d91906101d4565b60405180910390f35b610070600480360381019061006b9190610331565b610101565b005b60605f8054610080906103a5565b80601f01602080910402602001604051908101604052809291908181526020018280546100ac906103a5565b80156100f75780601f106100ce576101008083540402835291602001916100f7565b820191905f5260205f20905b8154815290600101906020018083116100da57829003601f168201915b5050505050905090565b805f908161010f919061057b565b507f26914d621a4d4d62e7da5969ebc8cf22159b205e02e032949891f10c5f25a3cc8160405161013f91906101d4565b60405180910390a150565b5f81519050919050565b5f82825260208201905092915050565b5f5b83811015610181578082015181840152602081019050610166565b5f8484015250505050565b5f601f19601f8301169050919050565b5f6101a68261014a565b6101b08185610154565b93506101c0818560208601610164565b6101c98161018c565b840191505092915050565b5f6020820190508181035f8301526101ec818461019c565b905092915050565b5f604051905090565b5f80fd5b5f80fd5b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b6102438261018c565b810181811067ffffffffffffffff821117156102625761026161020d565b5b80604052505050565b5f6102746101f4565b9050610280828261023a565b919050565b5f67ffffffffffffffff82111561029f5761029e61020d565b5b6102a88261018c565b9050602081019050919050565b828183375f83830152505050565b5f6102d56102d084610285565b61026b565b9050828152602081018484840111156102f1576102f0610209565b5b6102fc8482856102b5565b509392505050565b5f82601f83011261031857610317610205565b5b81356103288482602086016102c3565b91505092915050565b5f60208284031215610346576103456101fd565b5b5f82013567ffffffffffffffff81111561036357610362610201565b5b61036f84828501610304565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f60028204905060018216806103bc57607f821691505b6020821081036103cf576103ce610378565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026104317fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826103f6565b61043b86836103f6565b95508019841693508086168417925050509392505050565b5f819050919050565b5f819050919050565b5f61047f61047a61047584610453565b61045c565b610453565b9050919050565b5f819050919050565b61049883610465565b6104ac6104a482610486565b848454610402565b825550505050565b5f90565b6104c06104b4565b6104cb81848461048f565b505050565b5b818110156104ee576104e35f826104b8565b6001810190506104d1565b5050565b601f82111561053357610504816103d5565b61050d846103e7565b8101602085101561051c578190505b610530610528856103e7565b8301826104d0565b50505b505050565b5f82821c905092915050565b5f6105535f1984600802610538565b1980831691505092915050565b5f61056b8383610544565b9150826002028217905092915050565b6105848261014a565b67ffffffffffffffff81111561059d5761059c61020d565b5b6105a782546103a5565b6105b28282856104f2565b5f60209050601f8311600181146105e3575f84156105d1578287015190505b6105db8582610560565b865550610642565b601f1984166105f1866103d5565b5f5b82811015610618578489015182556001820191506020850194506020810190506105f3565b868310156106355784890151610631601f891682610544565b8355505b6001600288020188555050505b50505050505056fea2646970667358221220ecb0e169130032da04dc2619aa3780993d35cef6eb615b1211e962f6a531a39864736f6c63430008170033";

    public static final String FUNC_RETRIEVESTRING = "retrieveString";

    public static final String FUNC_UPLOADSTRING = "uploadString";

    public static final Event STRINGUPDATED_EVENT = new Event("StringUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected StringStorage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected StringStorage(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected StringStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected StringStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<StringUpdatedEventResponse> getStringUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Log> logs = transactionReceipt.getLogs();
        List<StringUpdatedEventResponse> responses = new ArrayList<>();

        for (Log log : logs) {
            StringUpdatedEventResponse typedResponse = getStringUpdatedEventFromLog(log);
            responses.add(typedResponse);
        }

        return responses;
    }

    public static StringUpdatedEventResponse getStringUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(STRINGUPDATED_EVENT, log);
        StringUpdatedEventResponse typedResponse = new StringUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.newString = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<StringUpdatedEventResponse> stringUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getStringUpdatedEventFromLog(log));
    }

    public Flowable<StringUpdatedEventResponse> stringUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STRINGUPDATED_EVENT));
        return stringUpdatedEventFlowable(filter);
    }

    public RemoteFunctionCall<String> retrieveString() {
        final Function function = new Function(FUNC_RETRIEVESTRING, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> uploadString(String newString) {
        final Function function = new Function(
                FUNC_UPLOADSTRING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(newString)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static StringStorage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new StringStorage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static StringStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new StringStorage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static StringStorage load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new StringStorage(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static StringStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new StringStorage(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<StringStorage> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(StringStorage.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<StringStorage> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(StringStorage.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<StringStorage> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(StringStorage.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<StringStorage> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(StringStorage.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class StringUpdatedEventResponse extends BaseEventResponse {
        public String newString;
    }
}
