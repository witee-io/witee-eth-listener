package com.mbr.chain.repository;

import com.mbr.chain.domain.bo.BlockNum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockNumRepository extends MongoRepository<BlockNum,Long>{
}
