const HealthRecordContract = artifacts.require("HealthRecordContract");

module.exports = function (deployer) {
  deployer.deploy(HealthRecordContract);
};
