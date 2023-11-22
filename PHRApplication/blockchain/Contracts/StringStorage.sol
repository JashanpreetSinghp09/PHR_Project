// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract StringStorage {
    // State variable to store the string
    string private storedString;

    // Event to log string updates
    event StringUpdated(string newString);

    // Function to upload a new string
    function uploadString(string memory newString) public {
        storedString = newString;
        emit StringUpdated(newString);
    }

    // Function to retrieve the stored string
    function retrieveString() public view returns (string memory) {
        return storedString;
    }
}