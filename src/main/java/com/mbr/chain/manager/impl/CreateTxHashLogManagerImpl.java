package com.mbr.chain.manager.impl;

import com.mbr.chain.common.utils.TimestampPkGenerator;
import com.mbr.chain.domain.bo.CreateTxHashLog;
import com.mbr.chain.manager.CreateTxHashLogManager;
import com.mbr.chain.repository.CreateTxHashLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("createTxHashLogManager")
public class CreateTxHashLogManagerImpl implements CreateTxHashLogManager{


    @Autowired
    private CreateTxHashLogRepository repository;

    public Long save(CreateTxHashLog createTxHashLog){
        createTxHashLog.setId(new TimestampPkGenerator().next(getClass()));
        createTxHashLog.setCreateTime(new Date());
        repository.save(createTxHashLog);
        return createTxHashLog.getId();
    }

}
