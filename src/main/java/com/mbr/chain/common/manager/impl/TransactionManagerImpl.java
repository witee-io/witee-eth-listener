package com.mbr.chain.common.manager.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;

import com.mbr.chain.common.manager.TransactionManager;
import com.mbr.chain.common.web3j.Web3jConnector;
import com.mbr.chain.domain.dto.SendTxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;


@Service
public class TransactionManagerImpl implements TransactionManager {

    @Autowired
    Web3jConnector web3Connector;


    @Override
    public BigInteger getNonce(String address) {
        try {
            address = Numeric.prependHexPrefix(address);
            EthGetTransactionCount count = this.web3Connector.connector().ethGetTransactionCount(
                    address, DefaultBlockParameterName.PENDING).send();

            return count.getTransactionCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String createTx(String addressFrom,
                           String gasPriceStr,
                           String gasLimitStr,
                           String addressTo,
                           String amountStr) throws Exception {
        addressFrom = Numeric.prependHexPrefix(addressFrom);
        addressTo = Numeric.prependHexPrefix(addressTo);
        BigInteger nonce = getNonce(addressFrom);
        BigInteger gasPrice = new BigInteger(gasPriceStr);
        BigInteger gasLimit = new BigInteger(gasLimitStr);
        BigInteger amount = new BigInteger(amountStr);
        return createTx(nonce, gasPrice, gasLimit, addressTo, amount);
    }

    private String createTx(BigInteger nonce,
                            BigInteger gasPrice,
                            BigInteger gasLimit,
                            String to,
                            BigInteger value) throws Exception {
//    	RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
//    	ObjectMapper mapper = new ObjectMapper();
//    	String json = mapper.writeValueAsString(rawTransaction);
//    	String encoded = Base64.getEncoder().encodeToString(json.getBytes("UTF-8"));
        return nonce.toString()+"&"+gasPrice.toString()+"&"+gasLimit.toString();
    }

    @Override
    public String createTxErc20(String addressFrom,
                                String gasPriceStr,
                                String gasLimitStr,
                                String addressTo,
                                String amountStr,
                                String tokenAddress) throws Exception {
        addressFrom = Numeric.prependHexPrefix(addressFrom);
        addressTo = Numeric.prependHexPrefix(addressTo);
        tokenAddress = Numeric.prependHexPrefix(tokenAddress);

        BigInteger nonce = getNonce(addressFrom);
        BigInteger gasPrice = new BigInteger(gasPriceStr);
        BigInteger gasLimit = new BigInteger(gasLimitStr);
        BigInteger amount = new BigInteger(amountStr);
        return createTxErc20(nonce, gasPrice, gasLimit, addressTo, amount, tokenAddress);
    }
    private String createTxErc20(BigInteger nonce,
                                 BigInteger gasPrice,
                                 BigInteger gasLimit,
                                 String to,
                                 BigInteger value,
                                 String tokenAddress) throws Exception {
        String data = encodeTransferEvent(value, to);
//        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, tokenAddress, data);
//    	ObjectMapper mapper = new ObjectMapper();
//    	String json = mapper.writeValueAsString(rawTransaction);
//    	String encoded = Base64.getEncoder().encodeToString(json.getBytes("UTF-8"));
        return nonce.toString()+"&"+gasPrice.toString()+"&"+gasLimit.toString()+"&"+data;
    }

    private String encodeTransferEvent(BigInteger value, String receiveAddress) {
        Address _receiveAddress = new Address(receiveAddress);
        Uint256 _value = new Uint256(value);
        Function function = new Function("transfer", Arrays.<Type>asList(_receiveAddress, _value), Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
        }));
        return FunctionEncoder.encode(function);
    }

    @Override
    public SendTxException sendTX(String signedTx) {
        SendTxException sendTxException = new SendTxException();
        try {
            EthSendTransaction tx = this.web3Connector.connector().ethSendRawTransaction(signedTx).send();
            if (tx == null) {
                sendTxException.setCode(500);
                sendTxException.setException("tx == null");
                return sendTxException;
            }
            if (tx.hasError()) {
                sendTxException.setCode(500);
                sendTxException.setException(tx.getError().getMessage());
                return sendTxException;
            }
            sendTxException.setCode(200);
            sendTxException.setException("tx == null");
            sendTxException.setData(tx.getTransactionHash());
            return sendTxException;

           // return tx.getTransactionHash();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sendTxException;
    }
}
