package com.mbr.chain.common.contract;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class ERC20Contract extends Contract {


    public ERC20Contract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public Utf8String name() throws IOException {
        Function function = new Function("name", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        return (Utf8String)this.executeCallSingleValueReturn(function);
    }

    public Utf8String symbol() throws IOException {
        Function function = new Function("symbol", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        return (Utf8String)this.executeCallSingleValueReturn(function);
    }

    public Uint256 decimals() throws IOException {
        Function function = new Function("decimals", Arrays.asList(), Arrays.asList(new TypeReference<Uint256>() {
        }));
        return (Uint256)this.executeCallSingleValueReturn(function);
    }

    public TransactionReceipt transfer(Address _to, Uint256 _value) throws IOException, TransactionException {
        Function function = new Function("transfer", Arrays.asList(_to, _value), Arrays.asList(new TypeReference<Bool>() {
        }));
        return this.executeTransaction(function);
    }

    public Uint256 totalSupply() throws IOException {
        Function function = new Function("totalSupply", Arrays.asList(), Arrays.asList(new TypeReference<Uint256>() {
        }));
        return (Uint256)this.executeCallSingleValueReturn(function);
    }

    public Uint256 balanceOf(Address addr) throws IOException {
        Function function = new Function("balanceOf", Arrays.asList(addr), Arrays.asList(new TypeReference<Uint256>() {
        }));
        return (Uint256)this.executeCallSingleValueReturn(function);
    }
}
