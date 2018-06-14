package com.mbr.chain.manager.impl;

import com.mbr.chain.domain.bo.BlockNum;
import com.mbr.chain.manager.BlockNumManager;
import com.mbr.chain.repository.BlockNumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Numeric;

@Service("blockNumManager")
public class BlockNumManagerImpl implements BlockNumManager{
    @Autowired
    private BlockNumRepository blockNumRepository;

    @Override
    public void save(String blockNumber) {
        BlockNum blockNum = new BlockNum();
        blockNum.setId(100000L);
        blockNum.setBlockNumber(Numeric.decodeQuantity(blockNumber).toString());
        blockNumRepository.save(blockNum);
    }

    @Override
    public Long queryBlockNumber() {
        BlockNum blockNum =  blockNumRepository.findOne(100000L);

        return Long.parseLong(blockNum.getBlockNumber()) ;
    }
}
