package com.mbr.chain.repository;

import com.mbr.chain.domain.bo.EthOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EthOrderRepository extends MongoRepository<EthOrder,Long> {



    //List<EthOrder> findByAddress(String address, Sort sort);

    EthOrder findByAddress(String address);

    EthOrder findByAddressAndDeviceId(String address,String deviceId);

    void deleteByDeviceId(String deviceId);

}
