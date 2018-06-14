package com.mbr.chain.domain.bo;

import lombok.Data;

import java.util.Date;

@Data
public class EthTokenOrder {

	private String coinId;
	private String coinName;
	private String tokenAddress;
	private Date createTime;
	private Date updateTime;

	/**
	 * @return the coinId
	 */
	public String getCoinId() {
		return coinId;
	}

	/**
	 * @param coinId
	 *            the coinId to set
	 */
	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	/**
	 * @return the coinName
	 */
	public String getCoinName() {
		return coinName;
	}

	/**
	 * @param coinName
	 *            the coinName to set
	 */
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	/**
	 * @return the tokenAddress
	 */
	public String getTokenAddress() {
		return tokenAddress;
	}

	/**
	 * @param tokenAddress
	 *            the tokenAddress to set
	 */
	public void setTokenAddress(String tokenAddress) {
		this.tokenAddress = tokenAddress;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
