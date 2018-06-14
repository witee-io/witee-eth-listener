package com.mbr.chain.queue;

import com.mbr.chain.domain.bo.BlockObservable;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockObservableQueue {

    private static int capacity = 800;

    public static LinkedBlockingQueue<BlockObservable> linkedBlockingQueue = new LinkedBlockingQueue(capacity);


}
