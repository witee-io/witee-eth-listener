package com.mbr.chain.manager;


import com.mbr.chain.domain.bo.BlockObservable;

import java.util.List;

public interface BlockObservableManager {

     void save(BlockObservable blockObservable);


     List<BlockObservable> findAll();

}
