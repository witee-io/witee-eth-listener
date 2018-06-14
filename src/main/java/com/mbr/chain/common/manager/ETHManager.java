package com.mbr.chain.common.manager;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

public interface ETHManager {


    public BigInteger getBalance(String address);

    public Transaction getTransactionByHash(String hash);

    public TransactionReceipt getTransactionReceipt(String hash);

    public EthBlock.Block getBlockByHash(String blockHash, boolean returnFullTransactionObjects);

    public BigInteger getBlockNumber();

}
