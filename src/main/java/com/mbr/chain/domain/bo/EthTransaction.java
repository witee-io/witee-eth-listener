package com.mbr.chain.domain.bo;

import java.math.BigInteger;

import lombok.Data;

@Data
public class EthTransaction extends Transaction {

	private String createTime;

	private String updateTime;
	private Long id;
	private Integer status;
	private Integer txStatus;

	private String orderId;

	private BigInteger confirmations;

	private String fee;

	private Integer height;

	private Boolean isErc20;
	private String tokenAddress;
	private String memo;
	private String decimals;
	private String signedTx;
	private String coinId;
	private Long channel;

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the txStatus
	 */
	public Integer getTxStatus() {
		return txStatus;
	}

	/**
	 * @param txStatus
	 *            the txStatus to set
	 */
	public void setTxStatus(Integer txStatus) {
		this.txStatus = txStatus;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the confirmations
	 */
	public BigInteger getConfirmations() {
		return confirmations;
	}

	/**
	 * @param confirmations
	 *            the confirmations to set
	 */
	public void setConfirmations(BigInteger confirmations) {
		this.confirmations = confirmations;
	}

	/**
	 * @return the fee
	 */
	public String getFee() {
		return fee;
	}

	/**
	 * @param fee
	 *            the fee to set
	 */
	public void setFee(String fee) {
		this.fee = fee;
	}

	/**
	 * @return the height
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * @return the isErc20
	 */
	public Boolean getIsErc20() {
		return isErc20;
	}

	/**
	 * @param isErc20
	 *            the isErc20 to set
	 */
	public void setIsErc20(Boolean isErc20) {
		this.isErc20 = isErc20;
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
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the decimals
	 */
	public String getDecimals() {
		return decimals;
	}

	/**
	 * @param decimals
	 *            the decimals to set
	 */
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}

	/**
	 * @return the signedTx
	 */
	public String getSignedTx() {
		return signedTx;
	}

	/**
	 * @param signedTx
	 *            the signedTx to set
	 */
	public void setSignedTx(String signedTx) {
		this.signedTx = signedTx;
	}

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

}
