package com.mbr.chain.common.manager;

import java.math.BigInteger;

public interface ERC20Manager {

    public BigInteger getBalance(String tokenAddress, String address);
}
