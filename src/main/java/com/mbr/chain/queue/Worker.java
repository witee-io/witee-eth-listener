package com.mbr.chain.queue;

import com.alibaba.fastjson.JSONObject;
import com.mbr.chain.domain.bo.BlockObservable;
import com.mbr.chain.manager.EthTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class Worker extends Thread{

    private EthTransactionManager ethTransactionManager;
    private LinkedBlockingQueue<BlockObservable> blockObservableQueue;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Worker(EthTransactionManager ethTransactionManager, LinkedBlockingQueue<BlockObservable> blockObservableQueue){
        this.ethTransactionManager = ethTransactionManager;
        this.blockObservableQueue = blockObservableQueue;
    }

    @Override
    public void run() {
        try {
            BlockObservable blockObservable =  blockObservableQueue.poll();
            logger.info("LinkedBlockingQueue  poll ->{}", JSONObject.toJSONString(blockObservable));
            if (blockObservable!=null) {
                ethTransactionManager.replayBlocksObservable(blockObservable);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
