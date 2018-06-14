package com.mbr.chain.manager;


import com.mbr.chain.domain.bo.BlockObservable;
import com.mbr.chain.domain.bo.ERC20TransferEventRecord;
import com.mbr.chain.domain.bo.EthTransaction;
import com.mbr.chain.domain.dto.request.TransactionQueryRequest;
import com.mbr.chain.domain.dto.request.TransactionRecord;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface EthTransactionManager {

    void saveSent(EthTransaction ethTransaction);

    void save(EthTransaction ethTransaction);

    void saveSendTx(EthTransaction ethTransaction);

    void updateEthTransaction(EthTransaction ethTransaction);

    //void updateSuccess(List<String> listHash, BigInteger height, BigInteger confirmations);

    void replayBlocksObservable(BlockObservable str);

    int maxHeight();

    public void saveErc20(ERC20TransferEventRecord record);

    EthTransaction queryById(Long id,Long channel);

    List<EthTransaction> queryRecord(TransactionRecord record);


    EthTransaction queryByHash(String hash,Long channel);

    Map<String,Object> queryTransaction(TransactionQueryRequest transactionQueryRequest);

    List<EthTransaction> queryByUnSuccess(int status);
}
