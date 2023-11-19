package com.example.phr_application;
/*
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ContractUtils {

    public static String loadBinary(String contractName) throws IOException, JSONException {
        // Adjust the path based on your project structure
        String filePath = "PHRApplication/blockchain/build/contracts/" + contractName + ".json";

        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        String jsonContent = new String(fileBytes);

        // Parse the JSON content and extract the bytecode
        JSONObject jsonObject = new JSONObject(jsonContent);

        // Replace "bytecode" with the actual field name in your JSON
        // The actual structure depends on the version of Truffle and Solidity compiler used
        String bytecode = jsonObject.getJSONObject("deployedBytecode").getString("object");

        return "0x" + bytecode;
    }
}
*/