package com.mbr.chain.configurer;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfigurer {

    @Bean
    public Queue txQueue(){
        return new Queue("txQueue");
    }

}