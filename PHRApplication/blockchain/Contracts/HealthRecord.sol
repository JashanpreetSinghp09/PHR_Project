// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract HealthRecord {
    // A struct to represent a health record
    struct Record {
        uint256 id;
        string data; // This can be a hash or encrypted data of the actual health record
        address createdBy; // To track which user (address) created the record
        uint256 timestamp; // To track when the record was created
    }

    // An array to store all health records
    Record[] private records;

    // Mapping from user address to list of record IDs they own
    mapping(address => uint256[]) private userRecords;

    // Event to be emitted when a new record is added
    event RecordAdded(uint256 indexed id, address indexed createdBy);

    /**
     * @dev Adds a new health record.
     * @param _data The health record data (preferably a hash or encrypted data).
     */
    function addRecord(string memory _data) public {
        uint256 recordId = records.length;
        records.push(Record(recordId, _data, msg.sender, block.timestamp));
        userRecords[msg.sender].push(recordId);

        emit RecordAdded(recordId, msg.sender);
    }

    /**
     * @dev Retrieves a specific health record by ID.
     * @param _id The ID of the record to retrieve.
     * @return The health record data.
     */
    function getRecord(uint256 _id) public view returns (string memory) {
        require(_id < records.length, "Record does not exist.");
        return records[_id].data;
    }

    /**
     * @dev Gets the list of record IDs owned by a user.
     * @param _user The address of the user.
     * @return The list of record IDs.
     */
    function getUserRecords(address _user) public view returns (uint256[] memory) {
        return userRecords[_user];
    }
}
