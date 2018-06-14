package com.mbr.chain.common.manager.impl;

import com.mbr.chain.common.contract.ERC20Contract;
import com.mbr.chain.common.manager.ERC20Manager;
import com.mbr.chain.common.web3j.Web3jConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Service
public class ERC20ManagerImpl implements ERC20Manager {

    @Autowired
    private Web3jConnector web3jConnector;

    @Override
    public BigInteger getBalance(String tokenAddress, String address) {
        try {
            tokenAddress = Numeric.prependHexPrefix(tokenAddress);
            Credentials credentials = Credentials.create("123213");
            ERC20Contract contract = new ERC20Contract(tokenAddress, this.web3jConnector.connector(), credentials, this.web3jConnector.getGasPrice(), this.web3jConnector.getGasLimit());
            Address destAddress = new Address(address);
            Uint256 balance = contract.balanceOf(destAddress);
            return balance == null ? BigInteger.ZERO : balance.getValue();
        } catch (Exception var6) {
            var6.printStackTrace();
            return BigInteger.ZERO;
        }
    }
}
