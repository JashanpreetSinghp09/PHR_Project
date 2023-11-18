module.exports = {
  networks: {
    development: {
      host: "127.0.0.1",
      port: 7545, // Update with your local blockchain network port (e.g., Ganache)
      network_id: "5777",
    },
  },
  compilers: {
    solc: {
      version: "0.8.0", // Update with your desired Solidity compiler version
      settings: {
        optimizer: {
          enabled: true,
          runs: 200,
        },
      },
    },
  },
};
