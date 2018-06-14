package com.mbr.chain.controller;

import com.mbr.chain.domain.bo.*;
import com.mbr.chain.domain.dto.BaseResult;
import com.mbr.chain.domain.dto.BaseResultCode;
import com.mbr.chain.domain.dto.request.TransactionQueryRequest;
import com.mbr.chain.domain.dto.request.TransactionRecord;
import com.mbr.chain.domain.dto.response.PageResult;
import com.mbr.chain.domain.dto.response.TransactionSendResponse;
import com.mbr.chain.manager.EthTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class TransactionController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EthTransactionManager ethTransactionManager;

    private static String format = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("sendTx")
    @ResponseBody
    public BaseResult<TransactionSendResponse> sendTx(@RequestBody EthTransaction sendTx) {
        ethTransactionManager.saveSent(sendTx);
        TransactionSendResponse transactionSendResponse = new TransactionSendResponse();
        transactionSendResponse.setTxId(sendTx.getHash());
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(BaseResultCode.SUCCESS);
        baseResult.setData(transactionSendResponse);
        return baseResult;
    }

    @PostMapping("queryRecord")
    @ResponseBody
    public BaseResult<List<EthTransaction>> queryRecord(@RequestBody TransactionRecord record){
        List<EthTransaction> list = this.ethTransactionManager.queryRecord(record);

        BaseResult baseResult = new BaseResult();
        baseResult.setCode(BaseResultCode.SUCCESS);
        baseResult.setData(list);
        return baseResult;
    }

    @PostMapping("queryTransaction")
    @ResponseBody
    public BaseResult<PageResult<EthTransaction>> queryTransaction(@RequestBody TransactionQueryRequest record){
        Map<String,Object> list = this.ethTransactionManager.queryTransaction(record);

        PageResult pageResult = new PageResult();
        pageResult.setTotal((Long)list.get("total"));
        pageResult.setList(list.get("list"));
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(BaseResultCode.SUCCESS);
        baseResult.setData(pageResult);
        return baseResult;
    }

    @PostMapping("queryHash")
    @ResponseBody
    public BaseResult<EthTransaction> queryHash(@RequestParam("hash") String hash,@RequestParam("channel") Long channel){
        EthTransaction ethTransaction = this.ethTransactionManager.queryByHash(hash,channel);
        BaseResult<EthTransaction> baseResult = new BaseResult<>();
        baseResult.setData(ethTransaction);
        return baseResult;

    }


    @PostMapping("queryByTransactionId")
    @ResponseBody
    public BaseResult<EthTransaction> queryByTransactionId(@RequestParam("transactionId") Long transactionId,@RequestParam("channel") Long channel){
        BaseResult<EthTransaction> baseResult = new BaseResult<>();
        EthTransaction ethTransaction = this.ethTransactionManager.queryById(transactionId,channel);
        baseResult.setData(ethTransaction);
        return baseResult;

    }

}
