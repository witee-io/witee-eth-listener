package com.mbr.chain.configurer;

import com.mbr.chain.listener.EthEventListener;
import com.mbr.chain.listener.EthSupplement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean(initMethod = "start",destroyMethod = "destroy")
    public EthEventListener getEthEventListener(){
        EthEventListener ethEventListener = new EthEventListener();
        return ethEventListener;
    }

//    @Bean(initMethod = "init")
//    public EthSupplement getEthSupplement(){
//        EthSupplement ethSupplement = new EthSupplement();
//        return ethSupplement;
//    }


}
