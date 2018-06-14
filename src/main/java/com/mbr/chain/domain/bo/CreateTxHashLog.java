package com.mbr.chain.domain.bo;

import java.util.Date;

import lombok.Data;

@Data
public class CreateTxHashLog {

	private Long id;
	private String txHash;
	private Date createTime;
	private String addressFrom;
	private String addressTo;
	private String tokenAddress;

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
	 * @return the txHash
	 */
	public String getTxHash() {
		return txHash;
	}

	/**
	 * @param txHash
	 *            the txHash to set
	 */
	public void setTxHash(String txHash) {
		this.txHash = txHash;
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
	 * @return the addressFrom
	 */
	public String getAddressFrom() {
		return addressFrom;
	}

	/**
	 * @param addressFrom
	 *            the addressFrom to set
	 */
	public void setAddressFrom(String addressFrom) {
		this.addressFrom = addressFrom;
	}

	/**
	 * @return the addressTo
	 */
	public String getAddressTo() {
		return addressTo;
	}

	/**
	 * @param addressTo
	 *            the addressTo to set
	 */
	public void setAddressTo(String addressTo) {
		this.addressTo = addressTo;
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

}
