package com.mbr.chain.domain.bo;

import lombok.Data;

@Data
public class BlockNum {
	private Long id;
	private String blockNumber;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the blockNumber
	 */
	public String getBlockNumber() {
		return blockNumber;
	}

	/**
	 * @param blockNumber
	 *            the blockNumber to set
	 */
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

}
