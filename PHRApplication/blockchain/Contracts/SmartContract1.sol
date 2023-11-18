// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract HealthRecordContract {

    // Struct to represent a patient's health record
    struct HealthRecord {
        string patientName;
        string diagnosis;
        string treatments;
        string medications;
        string allergies;
        string immunizations;
        string labResults;
        address doctor;
    }

    // Mapping to store health records for each patient
    mapping(address => HealthRecord) public healthRecords;

    // Event to log when a health record is updated
    event HealthRecordUpdated(address indexed patient, string diagnosis, string treatments, string medications, string allergies, string immunizations, string labResults, address doctor);

    // Function to update a patient's health record
    function updateHealthRecord(
        string memory _patientName,
        string memory _diagnosis,
        string memory _treatments,
        string memory _medications,
        string memory _allergies,
        string memory _immunizations,
        string memory _labResults
    ) public {
        // Ensure the patient has an existing health record
        require(bytes(healthRecords[msg.sender].patientName).length > 0, "Patient record not found");

        // Update the health record
        healthRecords[msg.sender] = HealthRecord({
            patientName: _patientName,
            diagnosis: _diagnosis,
            treatments: _treatments,
            medications: _medications,
            allergies: _allergies,
            immunizations: _immunizations,
            labResults: _labResults,
            doctor: msg.sender
        });

        // Emit an event to log the update
        emit HealthRecordUpdated(msg.sender, _diagnosis, _treatments, _medications, _allergies, _immunizations, _labResults, msg.sender);
    }

    // Function to create a new health record for a patient
    function createHealthRecord(string memory _patientName) public {
        // Ensure the patient does not already have a health record
        require(bytes(healthRecords[msg.sender].patientName).length == 0, "Patient record already exists");

        // Create a new health record
        healthRecords[msg.sender] = HealthRecord({
            patientName: _patientName,
            diagnosis: "",
            treatments: "",
            medications: "",
            allergies: "",
            immunizations: "",
            labResults: "",
            doctor: msg.sender
        });

        // Emit an event to log the creation
        emit HealthRecordUpdated(msg.sender, "", "", "", "", "", "", msg.sender);
    }

    // Function to retrieve a patient's health record
    function getHealthRecord(address patient) public view returns (
        string memory patientName,
        string memory diagnosis,
        string memory treatments,
        string memory medications,
        string memory allergies,
        string memory immunizations,
        string memory labResults,
        address doctor
    ) {
        HealthRecord memory record = healthRecords[patient];
        return (
            record.patientName,
            record.diagnosis,
            record.treatments,
            record.medications,
            record.allergies,
            record.immunizations,
            record.labResults,
            record.doctor
        );
    }
}
