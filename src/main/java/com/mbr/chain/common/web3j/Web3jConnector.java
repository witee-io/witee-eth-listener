package com.mbr.chain.common.web3j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

@Component
public class Web3jConnector {

    private Web3j web3j;

    @Value("${web3Url}")
    private String web3Url;

    @Value("${gasPrice}")
    private BigInteger gasPrice;

    @Value("${gasLimit}")
    private BigInteger gasLimit;


    private static final String GAS_LIMIT = "90000";

    private static final String GAS_PRICE = "20000000000";
    private Logger logger = LoggerFactory.getLogger(getClass());

    public synchronized Web3j connector(){
        if (this.web3j == null) {
            synchronized(Web3jConnector.class) {
                if (this.web3j == null) {
                    this.web3j = Web3j.build(new HttpService(this.web3Url));
                }
            }
        }

        return this.web3j;
    }

    public BigInteger getGasLimit() {

        BigInteger gasLimit;
        if (this.gasLimit == null) {
            gasLimit = new BigInteger(GAS_LIMIT);
        }else {
            gasLimit = this.gasLimit;
        }
        return gasLimit;
    }

    public BigInteger getGasPrice() {

        BigInteger gasPrice;
        if (this.gasPrice == null) {
            gasPrice = new BigInteger(GAS_PRICE);
        }else{
            gasPrice = this.gasPrice;
        }

        return gasPrice;
    }


    public String getWeb3Url() {
        return web3Url;
    }

    public void setWeb3Url(String web3Url) {
        this.web3Url = web3Url;
    }
}
