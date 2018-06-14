package com.mbr.chain.queue;


import com.alibaba.fastjson.JSONObject;
import com.mbr.chain.common.manager.ETHManager;
import com.mbr.chain.domain.TransactionStatus;
import com.mbr.chain.domain.bo.EthTransaction;
import com.mbr.chain.message.Sender;
import com.mbr.chain.repository.EthTransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class UpdateConfirmWorker  extends Thread{

    private EthTransactionRepository ethTransactionRepository;

    private  List<String> listHash;
    private BigInteger height;
    private  java.math.BigInteger confirmations;
    private ETHManager ethManager;
    private Sender sender;
    private static String format = "yyyy-MM-dd HH:mm:ss";
    private Logger logger = LoggerFactory.getLogger(getClass());

    public UpdateConfirmWorker(EthTransactionRepository ethTransactionRepository,
                               List<String> listHash,
                               BigInteger height,
                               BigInteger confirmations,
                               ETHManager ethManager,
                               Sender sender){
        this.ethTransactionRepository = ethTransactionRepository;
        this.listHash = listHash;
        this.confirmations=confirmations;
        this.ethManager= ethManager;
        this.sender = sender;
        this.height = height;
    }


    @Override
    public void run() {
        if (!listHash.isEmpty()) {
            List<EthTransaction> list = this.ethTransactionRepository.findByHashIn(listHash);
            if (list!=null&&list.size()>0){
                for (EthTransaction ethTransaction:list){
                    for (String hash:listHash){
                        if (StringUtils.isEmpty( ethTransaction.getHash())){
                            continue;
                        }
                        if (StringUtils.isEmpty(hash)){
                            continue;
                        }
                        if (ethTransaction.getHash().equals(hash)){
                            if (ethTransaction.getTxStatus() < TransactionStatus.TRANSACTION_SUCCESS) {
                                if (confirmations.intValue() >= 12) {
                                    ethTransaction.setConfirmations(confirmations);
                                    TransactionReceipt receipt = ethManager.getTransactionReceipt(ethTransaction.getHash());
                                    if (Numeric.decodeQuantity(receipt.getStatus()).intValue()==1) {
                                        BigInteger gasPrice = new BigInteger(ethTransaction.getGasPrice());
                                        BigInteger gasUsed = receipt.getGasUsed();
                                        ethTransaction.setFee(gasUsed.multiply(gasPrice).toString());
                                        ethTransaction.setTxStatus(TransactionStatus.TRANSACTION_SUCCESS);
                                    }else{
                                        ethTransaction.setTxStatus(TransactionStatus.TRANSACTION_FAILED);
                                    }
                                }
                                //发消息通知client客户端
                                sender.txQueue(JSONObject.toJSONString(ethTransaction));
                                logger.info("确认数:->{}", confirmations);
                                ethTransaction.setHeight(height.intValue());
                                ethTransaction.setConfirmations(confirmations);
                                ethTransaction.setUpdateTime(DateFormatUtils.format(new Date(), format));
                                ethTransactionRepository.save(ethTransaction);
                            }
                        }
                    }
                }
            }
//            for (String hash : listHash) {
//                EthTransaction ethTransaction = this.ethTransactionRepository.findByHash(hash);
//                if (ethTransaction == null) {
//                    continue;
//                }
//                if (ethTransaction.getTxStatus() < TransactionStatus.TRANSACTION_SUCCESS) {
//                    //if (ethTransaction.getHash().equals(hash)) {
//                    if (confirmations.intValue() >= 12) {
//                        ethTransaction.setTxStatus(TransactionStatus.TRANSACTION_SUCCESS);
//                        ethTransaction.setConfirmations(confirmations);
//                        TransactionReceipt receipt = ethManager.getTransactionReceipt(ethTransaction.getHash());
//                        if (!receipt.getStatus().equals("0x0")) {
//                            BigInteger gasPrice = new BigInteger(ethTransaction.getGasPrice());
//                            BigInteger gasUsed = receipt.getGasUsed();
//                            ethTransaction.setFee(gasUsed.multiply(gasPrice).toString());
//                        }
//                    }
//                    //发消息通知client客户端
//                    sender.txQueue(JSONObject.toJSONString(ethTransaction));
//                    logger.info("确认数:->{}", confirmations);
//                    ethTransaction.setHeight(height.intValue());
//                    ethTransaction.setConfirmations(confirmations);
//                    ethTransaction.setUpdateTime(DateFormatUtils.format(new Date(), format));
//                    ethTransactionRepository.save(ethTransaction);
//                }
//            }

        }
    }
}
