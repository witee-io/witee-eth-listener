package com.mbr.chain.listener;


import com.alibaba.fastjson.JSONObject;
import com.mbr.chain.common.manager.ETHManager;
import com.mbr.chain.common.utils.AddressUtil;
import com.mbr.chain.domain.TransactionStatus;
import com.mbr.chain.domain.bo.BlockObservable;
import com.mbr.chain.domain.bo.ERC20TransferEventRecord;
import com.mbr.chain.domain.bo.EthTransaction;
import com.mbr.chain.manager.BlockNumManager;
import com.mbr.chain.manager.EthTransactionManager;
import com.mbr.chain.message.Sender;
import com.mbr.chain.repository.EthTransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 重启补充逻辑
 */
@Component
public class EthSupplement {

    @Autowired
    private EthTransactionManager ethTransactionManager;

    @Autowired
    private BlockNumManager blockNumManager;

    @Autowired
    private ETHManager ethManager;

    private static String formt = "yyyy-MM-dd HH:mm:ss";

    private final int defaultBlockNum = 13;
    @Autowired
    private EthTransactionRepository ethTransactionRepository;

    @Autowired
    private Sender sender;

    @Value("${transactionTimeOut}")
    private int transactionTimeOut;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void start(){
        BigInteger maxBlockHeight = ethManager.getBlockNumber();

        long maxDbHeight = blockNumManager.queryBlockNumber();
        if(maxDbHeight < defaultBlockNum){
            maxDbHeight=0;
        }else{
            maxDbHeight = maxDbHeight - defaultBlockNum;
        }


        BlockObservable blockObservable = new BlockObservable();
        blockObservable.setStart(new BigInteger(maxDbHeight+""));
        blockObservable.setEnd(maxBlockHeight);
        blockObservable.setNumber(maxBlockHeight);

        ethTransactionManager.replayBlocksObservable(blockObservable);

    }

    //查询未确认的数据
    @Scheduled(cron = "0 */1 * * * ?")
    public void init(){
        logger.info("检查{}未确认的数据",transactionTimeOut);
       List<EthTransaction> list =  ethTransactionManager.queryByUnSuccess(TransactionStatus.TRANSACTION_SUCCESS);
       if (list!=null&&list.size()>0){
           for (EthTransaction ethTransaction :list){
               try {
                   //过了10分钟还没有确认的
                   if (StringUtils.isEmpty(ethTransaction.getCreateTime())){
                       continue;
                   }
                   Date d = DateUtils.parseDate(ethTransaction.getCreateTime(),formt);
                   Long currentTime  = new Date().getTime();
                   Long e = currentTime - d.getTime();
                   if(e >this.transactionTimeOut) {
                       if (StringUtils.isEmpty(ethTransaction.getHash())) {
                           continue;
                       }
                       TransactionReceipt receipt = ethManager.getTransactionReceipt(ethTransaction.getHash());
                       if (receipt==null){
                           continue;
                       }
                       if (!receipt.getStatus().equals("0x0")) {//转账成功的直接更新状态
                           if (StringUtils.isEmpty(ethTransaction.getGasPrice()) ){
                              continue;
                           }
                           BigInteger gasPrice = new BigInteger(ethTransaction.getGasPrice());
                           BigInteger gasUsed = receipt.getGasUsed();
                           ethTransaction.setFee(gasUsed.multiply(gasPrice).toString());
                           ethTransaction.setConfirmations(new BigInteger("12"));
                           ethTransaction.setUpdateTime(DateFormatUtils.format(new Date(), formt));
                           ethTransaction.setTxStatus(TransactionStatus.TRANSACTION_SUCCESS);
                           ethTransactionRepository.save(ethTransaction);
                            sender.txQueue(JSONObject.toJSONString(ethTransaction));
                       }
                   }
               } catch (ParseException e) {
                   logger.error("error", e);
               }
           }
       }

    }



}
