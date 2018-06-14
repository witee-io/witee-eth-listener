package com.mbr.chain.controller;

import com.alibaba.fastjson.JSONObject;
import com.mbr.chain.domain.bo.EthOrder;
import com.mbr.chain.domain.bo.EthTokenOrder;
import com.mbr.chain.domain.dto.request.ListenerAddressRequest;
import com.mbr.chain.domain.dto.request.ListenerAddressTokenRequest;
import com.mbr.chain.manager.EthOrderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class EthOrderController {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private EthOrderManager ethOrderManager;



    /**
     * 添加地址监听
     * @return
     */
    @PostMapping(value = "addListener")
    @ResponseBody
    public Object addListener(@RequestBody List<ListenerAddressRequest> listenerAddressRequests){
        logger.info("添加账号 postAccount---->{}", JSONObject.toJSONString(listenerAddressRequests));
        Map<String,Object> map = new HashMap<>();
        try {
            for (ListenerAddressRequest listenerAddressRequest: listenerAddressRequests) {
                EthOrder ethOrder = new EthOrder();
                ethOrder.setAddress(listenerAddressRequest.getAddress());//
                ethOrder.setChannel(listenerAddressRequest.getChannel());
                ethOrder.setDeviceId(listenerAddressRequest.getDeviceId());
                List<EthTokenOrder> list = new ArrayList<>();
                for (ListenerAddressTokenRequest request : listenerAddressRequest.getListenerAddressToken()) {
                    EthTokenOrder order = new EthTokenOrder();
                    order.setCoinName(request.getCoinName());
                    order.setCoinId(request.getCoinId());
                    order.setTokenAddress(request.getTokenAddress());
                    order.setCreateTime(new Date());
                    list.add(order);
                }
                ethOrder.setTokenOrders(list);
                ethOrder.setCreateTime(new Date());
                ethOrderManager.save(ethOrder,listenerAddressRequest.isSingle());
            }
            map.put("code","200");
            map.put("message","成功");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code","500");
            map.put("message",e.getMessage());
            return map;
        }
    }


}
