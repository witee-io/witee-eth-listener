package com.mbr.chain.repository;

import com.mbr.chain.domain.bo.EthOrderSent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EthOrderSentRepository extends MongoRepository<EthOrderSent,Long>{
    EthOrderSent findByAddress(String address);
}
