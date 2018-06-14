package com.mbr.chain.manager;


import com.mbr.chain.domain.bo.EthOrder;
import com.mbr.chain.domain.enums.AddOrderEnum;

import java.util.List;

public interface EthOrderManager {

    AddOrderEnum save(EthOrder EthOrder,boolean single)throws Exception;

    EthOrder findByAddress(String address);

    void deleteByDeviceId(String deviceId);

}
