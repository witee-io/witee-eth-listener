package com.mbr.chain.repository;

import com.mbr.chain.domain.bo.EthTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EthTransactionRepository extends MongoRepository<EthTransaction,Long> {


    EthTransaction findByHashAndChannel(String hash,Long channel);
    EthTransaction findByHash(String hash);

    List<EthTransaction> findByHashIn(List<String> hash);

    EthTransaction findByHashAndToAndTokenAddress(String hash ,String to,String tokenAddress);

    List<EthTransaction> findByTxStatusLessThan(int txStatus, Sort sort);

    List<EthTransaction> findByHeight(int height, Pageable pageable);

    Optional<EthTransaction> findByIdAndChannel(Long id,Long channel);

    List<EthTransaction> findByFromOrTo(String from,String to);

}
