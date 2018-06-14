package com.mbr.chain.common.manager.impl;

import com.mbr.chain.common.manager.ETHManager;
import com.mbr.chain.common.web3j.Web3jConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Service
public class ETHManagerImpl implements ETHManager {
    @Autowired
    Web3jConnector web3jConnector;



    public BigInteger getBalance(String address) {
        try {
            address = Numeric.prependHexPrefix(address);
            EthGetBalance ethGetBalance = this.web3jConnector.connector().ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            BigInteger value = ethGetBalance.getBalance();
            return value;
        } catch (Exception var3) {
            var3.printStackTrace();
            return BigInteger.ZERO;
        }
    }

    public Transaction getTransactionByHash(String hash) {
        try {
            EthTransaction transaction = this.web3jConnector.connector().ethGetTransactionByHash(hash).send();

            return transaction.getResult();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public TransactionReceipt getTransactionReceipt(String hash) {
        try {
            EthGetTransactionReceipt ethGetTransactionReceipt = this.web3jConnector.connector().ethGetTransactionReceipt(hash).send();
            return ethGetTransactionReceipt.getResult();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public EthBlock.Block getBlockByHash(String blockHash, boolean returnFullTransactionObjects) {
        try {
            EthBlock ethBlock = this.web3jConnector.connector().ethGetBlockByHash(blockHash, returnFullTransactionObjects).send();
            return ethBlock.getResult();
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public BigInteger getBlockNumber() {
        try {
            EthBlockNumber ethBlockNumber = this.web3jConnector.connector().ethBlockNumber().send();
            return ethBlockNumber.getBlockNumber();
        } catch (Exception var2) {
            var2.printStackTrace();
            return BigInteger.ZERO;
        }
    }
}
