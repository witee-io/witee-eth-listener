package com.mbr.chain.domain.bo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ERC20TransferEventRecord {

	private String tokenAddress;

	private String belongTransaction;

	private String from;

	private String to;

	private BigDecimal value;

	private Long id;
	private BigInteger blockNumber;
	private Date createTime;

	public String getTokenAddress() {
		return tokenAddress;
	}

	public void setTokenAddress(String tokenAddress) {
		this.tokenAddress = tokenAddress;
	}

	public String getBelongTransaction() {
		return belongTransaction;
	}

	public void setBelongTransaction(String belongTransaction) {
		this.belongTransaction = belongTransaction;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigInteger getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(BigInteger blockNumber) {
		this.blockNumber = blockNumber;
	}
}
