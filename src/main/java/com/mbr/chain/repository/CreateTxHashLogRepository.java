package com.mbr.chain.repository;

import com.mbr.chain.domain.bo.CreateTxHashLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateTxHashLogRepository extends MongoRepository<CreateTxHashLog,Long>{



}
