package com.mbr.chain.client.dto;

import lombok.Data;

@Data
public class ProductResponse {

	private Long id;
	/** 币名称 **/
	private String coinName;
	/** 币种类型，1是主链币，2是代币 **/
	private Integer coinType;
	/** 币logi **/
	private String coinAvatarUrl;
	/** 币描述 **/
	private String coinDescription;
	/** 是否erc20 **/
	private Integer coinErc20;
	/** 是否允许交易 **/
	private int onlineStatus;
	/** 链类型 **/
	private String chainType;
	/** 链地址 **/
	private String tokenAddress;
	/** 币精度 **/
	private Integer coinDecimals;



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
	 * @return the coinType
	 */
	public Integer getCoinType() {
		return coinType;
	}

	/**
	 * @param coinType
	 *            the coinType to set
	 */
	public void setCoinType(Integer coinType) {
		this.coinType = coinType;
	}

	/**
	 * @return the coinAvatarUrl
	 */
	public String getCoinAvatarUrl() {
		return coinAvatarUrl;
	}

	/**
	 * @param coinAvatarUrl
	 *            the coinAvatarUrl to set
	 */
	public void setCoinAvatarUrl(String coinAvatarUrl) {
		this.coinAvatarUrl = coinAvatarUrl;
	}

	/**
	 * @return the coinDescription
	 */
	public String getCoinDescription() {
		return coinDescription;
	}

	/**
	 * @param coinDescription
	 *            the coinDescription to set
	 */
	public void setCoinDescription(String coinDescription) {
		this.coinDescription = coinDescription;
	}

	/**
	 * @return the coinErc20
	 */
	public Integer getCoinErc20() {
		return coinErc20;
	}

	/**
	 * @param coinErc20
	 *            the coinErc20 to set
	 */
	public void setCoinErc20(Integer coinErc20) {
		this.coinErc20 = coinErc20;
	}

	/**
	 * @return the onlineStatus
	 */
	public int getOnlineStatus() {
		return onlineStatus;
	}

	/**
	 * @param onlineStatus
	 *            the onlineStatus to set
	 */
	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	/**
	 * @return the chainType
	 */
	public String getChainType() {
		return chainType;
	}

	/**
	 * @param chainType
	 *            the chainType to set
	 */
	public void setChainType(String chainType) {
		this.chainType = chainType;
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
	 * @return the coinDecimals
	 */
	public Integer getCoinDecimals() {
		return coinDecimals;
	}

	/**
	 * @param coinDecimals
	 *            the coinDecimals to set
	 */
	public void setCoinDecimals(Integer coinDecimals) {
		this.coinDecimals = coinDecimals;
	}

}
