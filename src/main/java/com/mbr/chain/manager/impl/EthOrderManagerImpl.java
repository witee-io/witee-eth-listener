package com.mbr.chain.manager.impl;

import com.mbr.chain.common.utils.TimestampPkGenerator;
import com.mbr.chain.domain.bo.EthOrder;
import com.mbr.chain.domain.bo.EthTokenOrder;
import com.mbr.chain.domain.enums.AddOrderEnum;
import com.mbr.chain.manager.EthOrderManager;
import com.mbr.chain.repository.EthOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class EthOrderManagerImpl implements EthOrderManager {

    @Autowired
    private EthOrderRepository ethOrderRepository;



    @Override
    public AddOrderEnum save(EthOrder ethOrder,boolean single) throws Exception {
        EthOrder o = ethOrderRepository.findByAddressAndDeviceId(ethOrder.getAddress(),ethOrder.getDeviceId());
        if (single){
            List<EthTokenOrder> old = o.getTokenOrders();
            old.addAll(ethOrder.getTokenOrders());
            ethOrderRepository.save(o);
        }else {
            if (o == null) {
                ethOrder.setId(new TimestampPkGenerator().next(getClass()));
            } else {
                ethOrder.setId(o.getId());
                ethOrder.setUpdateTime(new Date());
            }
            ethOrderRepository.save(ethOrder);
        }

        return AddOrderEnum.ORDER_SUCCESS;

    }

    @Override
    public EthOrder findByAddress(String address) {


        return ethOrderRepository.findByAddress(address);
    }

    @Override
    public void deleteByDeviceId(String deviceId) {
        ethOrderRepository.deleteByDeviceId(deviceId);
    }
}
