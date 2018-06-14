package com.mbr.chain.manager.impl;

import com.alibaba.fastjson.JSONObject;
import com.mbr.chain.client.ProductFeignClient;
import com.mbr.chain.client.dto.ProductResponse;
import com.mbr.chain.common.manager.ETHManager;
import com.mbr.chain.common.utils.AddressUtil;
import com.mbr.chain.common.utils.TimestampPkGenerator;
import com.mbr.chain.common.web3j.Web3jConnector;
import com.mbr.chain.domain.TransactionStatus;
import com.mbr.chain.domain.bo.*;
import com.mbr.chain.domain.dto.BaseResult;
import com.mbr.chain.domain.dto.request.TransactionQueryRequest;
import com.mbr.chain.domain.dto.request.TransactionRecord;
import com.mbr.chain.manager.EthTransactionManager;
import com.mbr.chain.message.Sender;
import com.mbr.chain.queue.UpdateConfirmWorker;
import com.mbr.chain.queue.Worker;
import com.mbr.chain.repository.BlockObservableRepository;
import com.mbr.chain.repository.EthOrderRepository;
import com.mbr.chain.repository.EthOrderSentRepository;
import com.mbr.chain.repository.EthTransactionRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;
import rx.Subscription;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;


//TODO 合约嵌套
@Service("ethTransactionManager")
public class EthTransactionManagerImpl implements EthTransactionManager {

    @Autowired
    private EthOrderRepository ethOrderRepository;

    @Autowired
    private EthTransactionRepository ethTransactionRepository;

    @Autowired
    private ETHManager ethManager;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Web3jConnector web3jConnector;

    @Autowired
    private Sender sender;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static String format = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private ProductFeignClient productFeignClient;

    private ExecutorService workerRegister;
    private int workerCount = 100;

    public EthTransactionManagerImpl(){
        workerRegister = Executors.newFixedThreadPool(workerCount);
    }

    @Override
    public void saveSent(EthTransaction ethTransaction) {
        String address = ethTransaction.getTo();
        if (StringUtils.isEmpty(address)){
            return;
        }
        ethTransaction.setId(new TimestampPkGenerator().next(getClass()));
        ethTransaction.setCreateTime(DateFormatUtils.format(new Date(),format));
        logger.info("saveSent 保存->{}",JSONObject.toJSONString(ethTransaction));
        ethTransactionRepository.save(ethTransaction);
    }

    @Override
    public void save(EthTransaction ethTransaction) {
        String address = ethTransaction.getTo();
        if (StringUtils.isEmpty(address)){
            return;
        }
        EthOrder o = ethOrderRepository.findByAddress(address);
        if (o!=null) {
            EthTransaction e = this.ethTransactionRepository.findByHash(ethTransaction.getHash());
            ethTransaction.setOrderId(o.getAddress());
            if (e==null){
                ethTransaction.setId(new TimestampPkGenerator().next(getClass()));
                ethTransaction.setCreateTime(DateFormatUtils.format(new Date(),format));
                ethTransaction.setIsErc20(false);
                ethTransaction.setChannel(o.getChannel());
                for (EthTokenOrder ethTokenOrder:o.getTokenOrders()){
                    if (ethTokenOrder.getCoinName().toUpperCase().equals("ETH")){
                        ethTransaction.setCoinId(ethTokenOrder.getCoinId());
                        break;
                    }
                }
                logger.info("保存交易数据e 为null->{}",JSONObject.toJSONString(ethTransaction));
            }else{
                ethTransaction.setId(e.getId());
                ethTransaction.setChannel(o.getChannel());
                ethTransaction.setUpdateTime(DateFormatUtils.format(new Date(),format));
                ethTransaction.setMemo(e.getMemo());
                ethTransaction.setCoinId(e.getCoinId());
                ethTransaction.setIsErc20(false);
                ethTransaction.setGas(e.getGas());
                ethTransaction.setGasPrice(e.getGasPrice());
                ethTransaction.setCoinId(e.getCoinId());
                ethTransaction.setDecimals(e.getDecimals());
                ethTransaction.setSignedTx(e.getSignedTx());
                ethTransaction.setCreateTime(e.getCreateTime());
                logger.info("保存交易数据e不为null->{}",JSONObject.toJSONString(ethTransaction));
            }

            ethTransactionRepository.save(ethTransaction);

        }

    }

    @Override
    public void saveSendTx(EthTransaction ethTransaction) {
        ethTransactionRepository.save(ethTransaction);
    }

    private void erc20Transfer(EthTransaction ethTransaction,ERC20TransferEventRecord r){

        String address = "";
        if (r == null){
            address = ethTransaction.getTo();
        }else{
            address = r.getTo();
        }

        String fromAddress = "";
        if (r == null){
            fromAddress = ethTransaction.getFrom();
        }else{
            fromAddress = r.getFrom();
        }
        if (StringUtils.isEmpty(address)){
            return;
        }
        EthOrder ethOrder = ethOrderRepository.findByAddress(address);//先查询转入
        if (ethOrder==null){
            ethOrder = ethOrderRepository.findByAddress(fromAddress);// 在查询转出
        }
        boolean b = false;
        String coinId = "";
        if (ethOrder!=null) {
            BaseResult<ProductResponse>  productResponseBaseResult=  productFeignClient.queryByTokenAddress(r.getTokenAddress());
            if (productResponseBaseResult!=null){
                logger.info("币信息->{}",JSONObject.toJSONString(productResponseBaseResult.getData()));
                ProductResponse response = productResponseBaseResult.getData();
                b = true;
                coinId = response.getId()+"";
            }

        }

        if (b) {
            //
            TransactionReceipt receiptErc20 = ethManager.getTransactionReceipt(ethTransaction.getHash());
            EthTransaction et = ethTransactionRepository.findByHash(ethTransaction.getHash());
            List<Log> logs = receiptErc20.getLogs();
            String status = receiptErc20.getStatus();
           // logger.info("status {} {}", status, ethTransaction.getHash());
            if (logs == null || logs.isEmpty()) {
                logger.info("no erc20 log {} {}", status, ethTransaction.getHash());
                return;
            }
           /* if (status.equals("0x0")){//失败
               // return;
                et.setTxStatus(TransactionStatus.TRANSACTION_FAILED);
            }
           */
            if (et == null) {
                et = new EthTransaction();
                et.setCreateTime(DateFormatUtils.format(new Date(), format));
                et.setId(new TimestampPkGenerator().next(getClass()));
                et.setStatus(0);
            } else {
                if (et.getTxStatus() == TransactionStatus.TRANSACTION_CONFIRMED||et.getTxStatus() == TransactionStatus.TRANSACTION_SUCCESS){//如果状态在确认中或者转账成功就直接return
                    return;
                }
                et.setUpdateTime(DateFormatUtils.format(new Date(), format));
            }
            et.setCoinId(coinId);
            if (Numeric.decodeQuantity(status).intValue()==0){
                et.setTxStatus(TransactionStatus.TRANSACTION_FAILED);
            } else {
                et.setTxStatus(TransactionStatus.TRANSACTION_CONFIRMED);
            }
            BigInteger gasPrice = new BigInteger(ethTransaction.getGasPrice());
            BigInteger gasUsed = receiptErc20.getGasUsed();
            et.setFee(gasUsed.multiply(gasPrice).toString());
            et.setChannel(ethOrder.getChannel());
            et.setValue(r.getValue().toString());
            et.setFrom(fromAddress);
            et.setTo(address);
            et.setTokenAddress(r.getTokenAddress());
            if (et.getConfirmations() == null) {
                et.setConfirmations(BigInteger.ONE);
            }
            et.setHash(ethTransaction.getHash());
            et.setNonce(ethTransaction.getNonce());
            et.setBlockHash(ethTransaction.getBlockHash());
            et.setBlockNumber(ethTransaction.getBlockNumber());
            et.setTransactionIndex(ethTransaction.getTransactionIndex());
            et.setGasPrice(ethTransaction.getGasPrice());
            et.setGas(ethTransaction.getGas());
            et.setCreates(ethTransaction.getCreates());
            et.setPublicKey(ethTransaction.getPublicKey());
            et.setRaw(ethTransaction.getRaw());
            et.setR(ethTransaction.getR());
            et.setS(ethTransaction.getS());
            et.setInput(ethTransaction.getInput());
            et.setV(ethTransaction.getV());
            et.setIsErc20(true);
            et.setOrderId(et.getFrom());
            logger.info("保存ERC20数据->{}",JSONObject.toJSONString(et));
            ethTransactionRepository.save(et);
        }

    }



    public static ERC20TransferEventRecord converter2ERC20TransferEvent(Log log) {

            ERC20TransferEventRecord erc20Transfer = new ERC20TransferEventRecord();
            erc20Transfer.setBelongTransaction(log.getTransactionHash());
            if (log.getTopics()!=null&&log.getTopics().size()>0) {
                try {
                    erc20Transfer.setFrom(AddressUtil.toAddress(log.getTopics().get(1)));
                    //erc20Transfer.setTo(AddressUtil.toAddress(log.getTopics().get(2)));
                } catch (Exception e) {
                    erc20Transfer.setFrom("");
                   // erc20Transfer.setTo("");
                }
                try {
                    //erc20Transfer.setFrom(AddressUtil.toAddress(log.getTopics().get(1)));
                    erc20Transfer.setTo(AddressUtil.toAddress(log.getTopics().get(2)));
                } catch (Exception e) {
                    //erc20Transfer.setFrom("");
                    erc20Transfer.setTo("");
                }
            }
            erc20Transfer.setTokenAddress(AddressUtil.toAddress(log.getAddress()));
            if (StringUtils.isNotEmpty(log.getData())) {
               if (isValidHexQuantity(log.getData())) {
                   erc20Transfer.setValue(new BigDecimal(Numeric.decodeQuantity(log.getData()).toString()));
               }
            }
            return erc20Transfer;

    }

    @Override
    public void updateEthTransaction(EthTransaction ethTransaction) {
        String address = ethTransaction.getTo();
        if (StringUtils.isEmpty(address)){
            return;
        }
        EthOrder o = ethOrderRepository.findByAddress(address);//转入
        if (o==null){
            o = ethOrderRepository.findByAddress(ethTransaction.getFrom());//转出
        }
        if (o!=null) {
            logger.info("updateEthTransaction -> {}",JSONObject.toJSONString(ethTransaction));
            EthTransaction et = ethTransactionRepository.findByHash(ethTransaction.getHash());
            if (et==null){
                et = new EthTransaction();
            }
            if (StringUtils.isEmpty(et.getCoinId())) {
                for (EthTokenOrder ethTokenOrder : o.getTokenOrders()) {
                    if (ethTokenOrder.getCoinName().toUpperCase().equals("ETH")) {
                        et.setCoinId(ethTokenOrder.getCoinId());
                        break;
                    }
                }
            }
            //
            TransactionReceipt receipt = ethManager.getTransactionReceipt(ethTransaction.getHash());
//            if (Numeric.decodeQuantity(receipt.getStatus()).equals("0")){
//                return;
//            }
            if (et != null) {
                if (et.getTxStatus() == TransactionStatus.TRANSACTION_CONFIRMED||et.getTxStatus() == TransactionStatus.TRANSACTION_SUCCESS){//如果状态在确认中或者转账成功就直接return
                    return;
                }
            }
            else {
                et.setId(new TimestampPkGenerator().next(getClass()));
                et.setCreateTime(DateFormatUtils.format(new Date(),format));
                et.setStatus(0);
            }

            String status = receipt.getStatus();
            if (Numeric.decodeQuantity(status).intValue()==0) {
                et.setTxStatus(TransactionStatus.TRANSACTION_FAILED);
            } else {
                et.setTxStatus(TransactionStatus.TRANSACTION_CONFIRMED);
            }

            if (et.getConfirmations() == null) {
                et.setConfirmations(BigInteger.ONE);
            } else {
                et.setConfirmations(et.getConfirmations());
            }
            et.setFrom(ethTransaction.getFrom());
            if (et.getIsErc20()==null){
                et.setIsErc20(false);
            }else {
                et.setIsErc20(et.getIsErc20());
            }
            if (et.getIsErc20()){
                TransactionReceipt receiptErc20 = ethManager.getTransactionReceipt(ethTransaction.getHash());
                List<Log> logs = receiptErc20.getLogs();
                if (logs!=null&&logs.size()>0) {
                    Log log = logs.get(0);
                    ERC20TransferEventRecord erc20TransferEventRecord = converter2ERC20TransferEvent(log);
                    et.setTo(erc20TransferEventRecord.getTo());
                    et.setValue(erc20TransferEventRecord.getValue().toString());
                    et.setTokenAddress(erc20TransferEventRecord.getTokenAddress());
                }
            }else{
                et.setTo(address);
                et.setValue(ethTransaction.getValue());
            }
            if (et.getConfirmations() == null) {
                et.setConfirmations(BigInteger.ONE);
            }
            BigInteger gasPrice = new BigInteger(ethTransaction.getGasPrice());
            BigInteger gasUsed = receipt.getGasUsed();
            et.setFee(gasUsed.multiply(gasPrice).toString());
            et.setChannel(o.getChannel());
            et.setHash(ethTransaction.getHash());
            et.setNonce(ethTransaction.getNonce());
            et.setBlockHash(ethTransaction.getBlockHash());
            et.setBlockNumber(ethTransaction.getBlockNumber());
            et.setTransactionIndex(ethTransaction.getTransactionIndex());
            et.setGasPrice(ethTransaction.getGasPrice());
            et.setGas(ethTransaction.getGas());
            et.setCreates(ethTransaction.getCreates());
            et.setPublicKey(ethTransaction.getPublicKey());
            et.setRaw(ethTransaction.getRaw());
            et.setR(ethTransaction.getR());
            et.setS(ethTransaction.getS());
            et.setInput(ethTransaction.getInput());
            et.setV(ethTransaction.getV());
            et.setOrderId(et.getFrom());
            logger.info("保存ETH 数据->{}",JSONObject.toJSONString(et));
            ethTransactionRepository.save(et);

        }

    }



//    @Override
//    public void updateSuccess(List<String> listHash, BigInteger height, BigInteger confirmations) {
//        if (listHash.isEmpty()){
//            return;
//        }
//
//        for (String hash : listHash) {
//            EthTransaction ethTransaction = this.ethTransactionRepository.findByHash(hash);
//            if (ethTransaction==null){
//                continue;
//            }
//            if (ethTransaction.getTxStatus()<TransactionStatus.TRANSACTION_SUCCESS) {
//                //if (ethTransaction.getHash().equals(hash)) {
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
//            //}
//
//        }
//
//    }

    @Override
    public void replayBlocksObservable(BlockObservable str) {
            BigInteger start = str.getStart();
            BigInteger end = str.getEnd();
            DefaultBlockParameter startBlockNumber = DefaultBlockParameter.valueOf(start);
            DefaultBlockParameter endBlockNumber = DefaultBlockParameter.valueOf(end);
            logger.info("replayBlocksObservable start->{},end->{}",start,end);
            web3jConnector.connector().replayBlocksObservable(startBlockNumber, endBlockNumber, true, true).subscribe(replayBlock -> {
                    try {
                        Response.Error errorReplay = replayBlock.getError();
                        if (errorReplay != null) {
                            logger.error(errorReplay.getMessage());
                        }
                        EthBlock.Block blockReplay = replayBlock.getBlock();
//                        if (str.getId()==null){//如果是新的交易
//                           List<EthBlock.TransactionResult> transactionResults =  blockReplay.getTransactions();
//                           for(EthBlock.TransactionResult et:transactionResults) {
//                               if (et instanceof EthBlock.TransactionObject) {
//                                   EthTransaction ethTransaction = new EthTransaction();
//                                   EthBlock.TransactionObject tx = (EthBlock.TransactionObject) et;
//                                   BeanUtils.copyProperties(tx,ethTransaction);
//                                   ethTransaction.setGasPrice(tx.getGasPrice().toString());
//                                   ethTransaction.setGas(tx.getGas().toString());
//                                   ethTransaction.setTransactionIndex(tx.getTransactionIndex().toString());
//                                   ethTransaction.setBlockNumber(tx.getBlockNumber().toString());
//                                   ethTransaction.setNonce(tx.getNonce().toString());
//                                   ethTransaction.setValue(tx.getValue().toString());
//                                   updateEthTransaction(ethTransaction);
//                               }
//                           }
//                        }
                        if (blockReplay != null) {// 更新状态
                            BigInteger height = blockReplay.getNumber();
                            BigInteger confirmations = str.getNumber().subtract(height).add(BigInteger.ONE);
                            //logger.info("tx hash-->{},确认数-->{}",blockReplay.getHash(),confirmations);
                            List<EthBlock.TransactionResult> txList = blockReplay.getTransactions();
                            List<String> hashList = new ArrayList<>();
                            for (EthBlock.TransactionResult result : txList) {
                                if (result instanceof EthBlock.TransactionObject) {
                                    EthBlock.TransactionObject object = (EthBlock.TransactionObject) result;
                                    //logger.info("监听hash TX ->{}",object.getHash());
                                    hashList.add(object.getHash());
                                    //TODO 发送交易到商家
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("txHash",object.getHash());
                                    map.put("fromAddr",object.getFrom());
                                    TransactionReceipt receiptErc20 = ethManager.getTransactionReceipt(object.getHash());
                                    if(receiptErc20==null){
                                        continue;
                                    }
                                    String status;
                                    //logger.info("hash->{},status->{},confirmations->{}",object.getHash(),Numeric.decodeQuantity(receiptErc20.getStatus()),confirmations);
                                    if (Numeric.decodeQuantity(receiptErc20.getStatus()).intValue()==0){
                                        status = "3";
                                         // 保存转账失败记录
                                        saveFail(object.getHash());
                                    }else{
                                        status = "2";
                                    }
                                    List<Log> logs = receiptErc20.getLogs();
                                    if (logs != null &&logs.size()>0) {
                                        Log log = logs.get(0);
                                        ERC20TransferEventRecord erc20TransferEventRecord = converter2ERC20TransferEvent(log);

                                        //logger.info("erc20 value==>{}",Numeric.decodeQuantity(log.getData()));
                                        if (erc20TransferEventRecord!=null) {
                                            map.put("value", erc20TransferEventRecord.getValue());
                                            map.put("toAddr", erc20TransferEventRecord.getTo());
                                        }

                                    }else{
                                        //logger.info("eth value==>{}",object.getValue());
                                        map.put("value",object.getValue());
                                        map.put("toAddr",object.getTo());
                                    }
                                    map.put("confirmations",confirmations);
                                    map.put("status",status);
                                    sender.transactionReceivedQueue(JSONObject.toJSONString(map));
                                }

                            }
                            if(!hashList.isEmpty()) {
                                //logger.info("==>hash ===>{},confirmations->{}",JSONObject.toJSONString(hashList),confirmations);
                                //updateSuccess(hashList, height, confirmations);

                                UpdateConfirmWorker worker = new UpdateConfirmWorker(ethTransactionRepository,hashList,height,confirmations,ethManager,sender);
                                worker.setDaemon(true);
                                workerRegister.execute(worker);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> {
                    err.printStackTrace();
                },
                () -> {


                });



    }

    @Override
    public int maxHeight() {
        Sort sort = new Sort(Sort.Direction.DESC,"height");
        Pageable pageable = new PageRequest(0,2,sort);
        Page<EthTransaction> pageList = this.ethTransactionRepository.findAll(pageable);
        List<EthTransaction> list = pageList.getContent();
        return list!=null&&list.size()>0?list.get(0).getHeight():0;
    }

    /**
     * 保存转账失败记录
     * @param hash
     */
    private void saveFail(String hash){
        EthTransaction e = ethTransactionRepository.findByHash(hash);
        //判断hash 有没有保存
        if (e!=null){
            return;
        }
        Transaction tx = ethManager.getTransactionByHash(hash);
        String address = tx.getTo();
        if (StringUtils.isEmpty(address)){
            return;
        }
        EthOrder o = ethOrderRepository.findByAddress(address);//转入
        if (o==null){
            o = ethOrderRepository.findByAddress(tx.getFrom());//转出
        }
        if (o!=null) {
            EthTransaction ethTransaction = new EthTransaction();
            ethTransaction.setId(new TimestampPkGenerator().next(getClass()));
            ethTransaction.setHash(tx.getHash());
            ethTransaction.setNonce(tx.getNonce().toString());
            ethTransaction.setBlockHash(tx.getBlockHash());
            ethTransaction.setBlockNumber(tx.getBlockNumber().toString());
            ethTransaction.setTransactionIndex(tx.getTransactionIndex().toString());
            ethTransaction.setFrom(tx.getFrom());
            ethTransaction.setTo(tx.getTo());
            ethTransaction.setValue(tx.getValue() == null ? "0" : tx.getValue().toString());
            ethTransaction.setGasPrice(tx.getGasPrice().toString());
            ethTransaction.setGas(tx.getGas().toString());
            ethTransaction.setInput(tx.getInput());
            ethTransaction.setCreates(tx.getCreates());
            ethTransaction.setPublicKey(tx.getPublicKey());
            ethTransaction.setRaw(tx.getRaw());
            ethTransaction.setR(tx.getR());
            ethTransaction.setS(tx.getS());
            ethTransaction.setV(tx.getV());
            ethTransaction.setTxStatus(TransactionStatus.TRANSACTION_FAILED);
            ethTransaction.setCreateTime(DateFormatUtils.format(new Date(), format));
            this.ethTransactionRepository.save(ethTransaction);

        }
    }

    @Override
    public void saveErc20(ERC20TransferEventRecord record) {
        Transaction tx = ethManager.getTransactionByHash(record.getBelongTransaction());
        EthTransaction ethTransaction = new EthTransaction();
        if (tx!=null) {
            ethTransaction.setHash(tx.getHash());
            ethTransaction.setNonce(tx.getNonce().toString());
            ethTransaction.setBlockHash(tx.getBlockHash());
            ethTransaction.setBlockNumber(tx.getBlockNumber().toString());
            ethTransaction.setTransactionIndex(tx.getTransactionIndex().toString());
            ethTransaction.setFrom(tx.getFrom());
            ethTransaction.setTo(record.getTo() == null ? "" : record.getTo());
            ethTransaction.setValue(record.getValue() == null ? "0" : record.getValue().toString());
            ethTransaction.setGasPrice(tx.getGasPrice().toString());
            ethTransaction.setGas(tx.getGas().toString());
            ethTransaction.setInput(tx.getInput());
            ethTransaction.setCreates(tx.getCreates());
            ethTransaction.setPublicKey(tx.getPublicKey());
            ethTransaction.setRaw(tx.getRaw());
            ethTransaction.setR(tx.getR());
            ethTransaction.setS(tx.getS());
            ethTransaction.setV(tx.getV());
            ethTransaction.setCreateTime(DateFormatUtils.format(new Date(), format));
            ethTransaction.setTokenAddress(record.getTokenAddress() == null ? "" : record.getTokenAddress());
            this.erc20Transfer(ethTransaction, record);
        }

    }

    @Override
    public EthTransaction queryById(Long id,Long channel) {
        return this.ethTransactionRepository.findByIdAndChannel(id,channel).get();
    }

    @Override
    public List<EthTransaction> queryRecord(TransactionRecord record) {
        logger.info("查询转账记录->{}",JSONObject.toJSONString(record));
        Query query = new Query();
        Criteria c = new Criteria();
        if (StringUtils.isEmpty(record.getType())){
            Object[] from = new Object[record.getFrom().size()];
            if (record.getFrom()!=null&&record.getFrom().size()>0) {
                for (int i = 0; i < record.getFrom().size(); i++) {
                    from[i] = record.getFrom().get(i);
                }
            }
            Object[] to = new Object[record.getTo().size()];
            if (record.getTo()!=null&&record.getTo().size()>0) {
                for (int i = 0; i < record.getTo().size(); i++) {
                    to[i] = record.getTo().get(i);
                }

            }
            if (from.length>0&&to.length>0) {
                c.orOperator(Criteria.where("from").in(from), Criteria.where("to").in(to));
            }

        }

        if (StringUtils.isNotEmpty(record.getType())) {
            if (record.getType().equals("1")) {//转入
                if (record.getFrom() != null && record.getFrom().size() > 0) {
                    if (record.getFrom().size() > 0) {
                        Object[] from = new String[record.getFrom().size()];
                        for (int i = 0; i < record.getFrom().size(); i++) {
                            from[i] = record.getFrom().get(i);
                        }
                        c.orOperator(Criteria.where("from").in(from));
                    }
                }
            }
            if (record.getType().equals("0")) {//转出
                if (record.getTo() != null && record.getTo().size() > 0) {
                    if (record.getTo().size() > 0) {
                        Object[] to = new String[record.getTo().size()];
                        for (int i = 0; i < record.getTo().size(); i++) {
                            to[i] = record.getTo().get(i);
                        }
                        c.orOperator(Criteria.where("to").in(to));
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(record.getTime())){
            //Pattern pattern = Pattern.compile("/^2018-04.*/i", Pattern.CASE_INSENSITIVE);
            Pattern pattern = Pattern.compile("^.*" + record.getTime() + ".*$");
            c.and("createTime").regex(pattern);
        }
        if (StringUtils.isNotEmpty(record.getTokenAddress())){
            c.and("tokenAddress").is(record.getTokenAddress());
        }
        if (StringUtils.isNotEmpty(record.getCoinId())){
            c.and("coinId").is(record.getCoinId());
        }
        if (StringUtils.isNotEmpty(record.getStatus())){
           /* List list = new ArrayList();
            list.add(record.getStatus());
            c.andOperator(Criteria.where("txStatus").in(list));*/

            if (record.getStatus().equals("1")){
                c.and("txStatus").lt(TransactionStatus.TRANSACTION_SUCCESS);
            }
            if (record.getStatus().equals("2")){
                c.and("txStatus").is(TransactionStatus.TRANSACTION_SUCCESS);
            }

        }
        if (record.getChannel()!=null) {
            c.and("channel").is(record.getChannel());
        }
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"createTime")));
        query.addCriteria(c);

        return mongoTemplate.find(query,EthTransaction.class);

    }

    @Override
    public EthTransaction queryByHash(String hash,Long channel) {
        return this.ethTransactionRepository.findByHashAndChannel(hash,channel);
    }

    @Override
    public Map<String,Object> queryTransaction(TransactionQueryRequest record) {

        Query query = new Query();
        Criteria c = new Criteria();
        if (StringUtils.isNotEmpty(record.getFrom())){
            c.orOperator(Criteria.where("from").is(record.getFrom()));
        }
        if (StringUtils.isNotEmpty(record.getTo())){
            c.orOperator(Criteria.where("to").is(record.getTo()));
        }
        if (StringUtils.isEmpty(record.getFrom())&&StringUtils.isEmpty(record.getTo())){
                c.orOperator(Criteria.where("from").is(record.getFrom()), Criteria.where("to").is(record.getTo()));
        }
        c.andOperator(Criteria.where("channel").is(record.getChannel()));
        int pageNo = record.getPageNo()-1;
        Pageable pageable = new PageRequest(pageNo,record.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC,"createTime")));

        query.with(pageable);
        query.addCriteria(c);
       // Page page = mongoTemplate.find(query,EthTransaction.class);
        List<EthTransaction> list = mongoTemplate.find(query,EthTransaction.class);
        Long count = mongoTemplate.count(query,EthTransaction.class);
        Map<String,Object> map = new HashMap<>();
        map.put("total",count);
        map.put("list",list);
        return map;
    }

    @Override
    public List<EthTransaction> queryByUnSuccess(int status) {
        Sort sort = new Sort(Sort.Direction.ASC,"createTime");
        return ethTransactionRepository.findByTxStatusLessThan(status,sort);
    }

    private static boolean isValidHexQuantity(String value) {
        if (value == null) {
            return false;
        } else if (value.length() < 3) {
            return false;
        } else {
            return value.startsWith("0x");
        }
    }
}
