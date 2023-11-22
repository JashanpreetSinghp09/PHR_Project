package com.example.phr_application;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
public class BlockTextUploader extends Contract {
    public static final String BINARY = "0x608060405234801561000f575f80fd5b506106458061001d5f395ff3fe608060405234801561000f575f80fd5b5060043610610034575f3560e01c806327e9321914610038578063a1557b9414610054575b5f80fd5b610052600480360381019061004d919061025c565b610072565b005b61005c610084565b604051610069919061031d565b60405180910390f35b805f90816100809190610540565b5050565b5f80546100909061036a565b80601f01602080910402602001604051908101604052809291908181526020018280546100bc9061036a565b80156101075780601f106100de57610100808354040283529160200191610107565b820191905f5260205f20905b8154815290600101906020018083116100ea57829003601f168201915b505050505081565b5f604051905090565b5f80fd5b5f80fd5b5f80fd5b5f80fd5b5f601f19601f8301169050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b61016e82610128565b810181811067ffffffffffffffff8211171561018d5761018c610138565b5b80604052505050565b5f61019f61010f565b90506101ab8282610165565b919050565b5f67ffffffffffffffff8211156101ca576101c9610138565b5b6101d382610128565b9050602081019050919050565b828183375f83830152505050565b5f6102006101fb846101b0565b610196565b90508281526020810184848401111561021c5761021b610124565b5b6102278482856101e0565b509392505050565b5f82601f83011261024357610242610120565b5b81356102538482602086016101ee565b91505092915050565b5f6020828403121561027157610270610118565b5b5f82013567ffffffffffffffff81111561028e5761028d61011c565b5b61029a8482850161022f565b91505092915050565b5f81519050919050565b5f82825260208201905092915050565b5f5b838110156102da5780820151818401526020810190506102bf565b5f8484015250505050565b5f6102ef826102a3565b6102f981856102ad565b93506103098185602086016102bd565b61031281610128565b840191505092915050565b5f6020820190508181035f83015261033581846102e5565b905092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061038157607f821691505b6020821081036103945761039361033d565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026103f67fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826103bb565b61040086836103bb565b95508019841693508086168417925050509392505050565b5f819050919050565b5f819050919050565b5f61044461043f61043a84610418565b610421565b610418565b9050919050565b5f819050919050565b61045d8361042a565b6104716104698261044b565b8484546103c7565b825550505050565b5f90565b610485610479565b610490818484610454565b505050565b5b818110156104b3576104a85f8261047d565b600181019050610496565b5050565b601f8211156104f8576104c98161039a565b6104d2846103ac565b810160208510156104e1578190505b6104f56104ed856103ac565b830182610495565b50505b505050565b5f82821c905092915050565b5f6105185f19846008026104fd565b1980831691505092915050565b5f6105308383610509565b9150826002028217905092915050565b610549826102a3565b67ffffffffffffffff81111561056257610561610138565b5b61056c825461036a565b6105778282856104b7565b5f60209050601f8311600181146105a8575f8415610596578287015190505b6105a08582610525565b865550610607565b601f1984166105b68661039a565b5f5b828110156105dd578489015182556001820191506020850194506020810190506105b8565b868310156105fa57848901516105f6601f891682610509565b8355505b6001600288020188555050505b50505050505056fea26469706673582212202531bf47fe3cbe975a6915726ee34c1a1fafd127970fb023286940567d844c7564736f6c63430008150033";

    public static final String FUNC_UPLOADEDTEXT = "uploadedText";

    public static final String FUNC_UPLOADTEXT = "uploadText";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected BlockTextUploader(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BlockTextUploader(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BlockTextUploader(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BlockTextUploader(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> uploadedText() {
        final Function function = new Function(FUNC_UPLOADEDTEXT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> uploadText(String newText) {
        final Function function = new Function(
                FUNC_UPLOADTEXT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(newText)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static BlockTextUploader load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BlockTextUploader(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BlockTextUploader load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BlockTextUploader(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BlockTextUploader load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BlockTextUploader(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BlockTextUploader load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BlockTextUploader(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BlockTextUploader> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BlockTextUploader.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BlockTextUploader> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BlockTextUploader.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<BlockTextUploader> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BlockTextUploader.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BlockTextUploader> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BlockTextUploader.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
