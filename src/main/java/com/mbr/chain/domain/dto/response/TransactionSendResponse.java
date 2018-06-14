package com.mbr.chain.domain.dto.response;

import lombok.Data;

@Data
public class TransactionSendResponse {
	private String txId;

	/**
	 * @return the txId
	 */
	public String getTxId() {
		return txId;
	}

	/**
	 * @param txId
	 *            the txId to set
	 */
	public void setTxId(String txId) {
		this.txId = txId;
	}
}
