const HealthRecord = artifacts.require("HealthRecord");

module.exports = function (deployer) {
  deployer.deploy(HealthRecord);
};
