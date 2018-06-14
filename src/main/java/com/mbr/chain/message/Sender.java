package com.mbr.chain.message;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void transactionReceivedQueue(String json) {

        this.rabbitTemplate.convertAndSend("transactionReceivedQueue", json);
    }


    public void txQueue(String json) {

        this.rabbitTemplate.convertAndSend("txQueue", json);
    }
}

