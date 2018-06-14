package com.mbr.chain.domain.dto.request;

import lombok.Data;

@Data
public class TransactionRequest {

	private String coinId;
	private String addressFrom;// 转出账户地址
	private String addressTo;// 转入账户地址
	private String amount;// 转账数量（单位wei）
	private String standard;// 链类型
	private String gasPrice;// ETH专有字段 gas单价（单位wei）
	private String gasLimit;// ETH专有字段 gas个数上限
	private String memo;// 备注
	private String fee;
	private String decimals;

	private String signedTx;
	private String tokenAddress;
	private String withdraw;
	private Long merchantId;

	public static class TransactionTypeCode {
		public final static String ETH_TYPE = "ETH";

		public final static String ERC20_TYPE = "ERC20";
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
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the standard
	 */
	public String getStandard() {
		return standard;
	}

	/**
	 * @param standard
	 *            the standard to set
	 */
	public void setStandard(String standard) {
		this.standard = standard;
	}

	/**
	 * @return the gasPrice
	 */
	public String getGasPrice() {
		return gasPrice;
	}

	/**
	 * @param gasPrice
	 *            the gasPrice to set
	 */
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}

	/**
	 * @return the gasLimit
	 */
	public String getGasLimit() {
		return gasLimit;
	}

	/**
	 * @param gasLimit
	 *            the gasLimit to set
	 */
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
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
	 * @return the withdraw
	 */
	public String getWithdraw() {
		return withdraw;
	}

	/**
	 * @param withdraw
	 *            the withdraw to set
	 */
	public void setWithdraw(String withdraw) {
		this.withdraw = withdraw;
	}

	/**
	 * @return the merchantId
	 */
	public Long getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId
	 *            the merchantId to set
	 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
