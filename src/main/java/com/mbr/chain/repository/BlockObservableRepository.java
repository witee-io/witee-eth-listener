package com.mbr.chain.repository;

import com.mbr.chain.domain.bo.BlockObservable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockObservableRepository  extends MongoRepository<BlockObservable,Long>{

    void deleteById(Long id);
}
