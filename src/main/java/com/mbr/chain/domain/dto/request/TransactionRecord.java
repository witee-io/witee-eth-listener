package com.mbr.chain.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class TransactionRecord {

	private List<String> from;
	private List<String> to;
	private String time;

	private String type;// 0 转入，1 转出

	private String status;// 1 处理中 2 支付成功

	private String coinId;

	private String tokenAddress;
	private Long channel;

	/**
	 * @return the from
	 */
	public List<String> getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(List<String> from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
