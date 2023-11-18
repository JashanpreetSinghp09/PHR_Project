const HealthRecord = artifacts.require("HealthRecord");

contract("HealthRecord", accounts => {
    let healthRecordInstance;

    beforeEach(async () => {
        healthRecordInstance = await HealthRecord.new();
    });

    it("should add a health record and retrieve it", async () => {
        const testData = "hash-of-record";
        await healthRecordInstance.addRecord(testData, { from: accounts[0] });

        const record = await healthRecordInstance.getRecord(0);
        assert.equal(record, testData, "The data of the retrieved record does not match the expected value.");
    });

    it("should maintain immutability of records", async () => {
        const testData1 = "hash-of-record-1";
        const testData2 = "hash-of-record-2";
        await healthRecordInstance.addRecord(testData1, { from: accounts[0] });
        await healthRecordInstance.addRecord(testData2, { from: accounts[0] });

        const record1 = await healthRecordInstance.getRecord(0);
        const record2 = await healthRecordInstance.getRecord(1);
        assert.equal(record1, testData1, "The data of the first record should be immutable.");
        assert.equal(record2, testData2, "The data of the second record should be immutable.");
    });

    it("should prevent retrieval of non-existent records", async () => {
        try {
            await healthRecordInstance.getRecord(0);
            assert.fail("The contract did not throw as expected");
        } catch (error) {
            assert.include(error.message, "Record does not exist.", "Contract should throw an error for non-existent record.");
        }
    });
});
