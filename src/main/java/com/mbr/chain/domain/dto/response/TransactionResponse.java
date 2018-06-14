package com.mbr.chain.domain.dto.response;

import lombok.Data;

@Data
public class TransactionResponse {

    private String txDbId;
    private String txParams;
	/**
	 * @return the txDbId
	 */
	public String getTxDbId() {
		return txDbId;
	}
	/**
	 * @param txDbId the txDbId to set
	 */
	public void setTxDbId(String txDbId) {
		this.txDbId = txDbId;
	}
	/**
	 * @return the txParams
	 */
	public String getTxParams() {
		return txParams;
	}
	/**
	 * @param txParams the txParams to set
	 */
	public void setTxParams(String txParams) {
		this.txParams = txParams;
	}



}
