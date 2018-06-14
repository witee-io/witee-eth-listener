package com.mbr.chain.common.manager;

import com.mbr.chain.domain.dto.SendTxException;

import java.math.BigInteger;

public interface TransactionManager {

    public BigInteger getNonce(String address);

    public String createTx(String addressFrom, String gasPriceStr, String gasLimitStr, String addressTo, String amountStr) throws Exception ;


    public String createTxErc20(String addressFrom, String gasPriceStr, String gasLimitStr, String addressTo, String amountStr, String tokenAddress) throws Exception;

    public SendTxException sendTX(String signedTx);
}
