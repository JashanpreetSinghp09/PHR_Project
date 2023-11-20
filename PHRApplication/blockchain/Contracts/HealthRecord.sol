// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract HealthRecord {
    struct LabResult {
        uint256 id;
        string result;
    }

    mapping(address => LabResult[]) private userLabResults;

    function uploadLabResult(string memory result) public {
        uint256 resultId = userLabResults[msg.sender].length;
        userLabResults[msg.sender].push(LabResult(resultId, result));
    }

    function getLabResult(uint256 resultId) public view returns (string memory) {
        require(resultId < userLabResults[msg.sender].length, "Result does not exist.");
        return userLabResults[msg.sender][resultId].result;
    }

    function getUserLabResultsCount() public view returns (uint256) {
        return userLabResults[msg.sender].length;
    }
}
