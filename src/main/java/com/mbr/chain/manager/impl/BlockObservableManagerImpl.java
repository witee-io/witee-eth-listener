package com.mbr.chain.manager.impl;

import com.mbr.chain.common.utils.TimestampPkGenerator;
import com.mbr.chain.domain.bo.BlockObservable;
import com.mbr.chain.manager.BlockObservableManager;
import com.mbr.chain.repository.BlockObservableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("blockObservableManager")
public class BlockObservableManagerImpl implements BlockObservableManager {

    @Autowired
    private BlockObservableRepository blockObservableRepository;

    @Override
    public void save(BlockObservable blockObservable) {
        blockObservable.setId(new TimestampPkGenerator().next(getClass()));
        blockObservableRepository.save(blockObservable);
    }

    @Override
    public List<BlockObservable> findAll() {
        Sort sort = new Sort(Sort.Direction.ASC,"start");
        return blockObservableRepository.findAll(sort);
    }
}
