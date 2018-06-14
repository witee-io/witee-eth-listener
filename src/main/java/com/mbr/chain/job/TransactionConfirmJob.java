package com.mbr.chain.job;

import com.mbr.chain.manager.EthTransactionManager;
import com.mbr.chain.queue.BlockObservableQueue;
import com.mbr.chain.queue.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class TransactionConfirmJob {

    @Autowired
    private EthTransactionManager ethTransactionManager;
    private ExecutorService workerRegister;
    private int workerCount = 100;

//    @Scheduled(cron = "0/5 * * * * *")
//    public void exec(){
//        workerRegister = Executors.newFixedThreadPool(workerCount);
//
//        Worker worker = new Worker(ethTransactionManager, BlockObservableQueue.linkedBlockingQueue);
//        worker.setDaemon(true);
//
//        workerRegister.execute(worker);
//
//    }

}
