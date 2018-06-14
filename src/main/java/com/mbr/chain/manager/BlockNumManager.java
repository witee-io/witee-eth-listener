package com.mbr.chain.manager;

public interface BlockNumManager {
    public void save(String blockNumber);

    public Long queryBlockNumber();
}
