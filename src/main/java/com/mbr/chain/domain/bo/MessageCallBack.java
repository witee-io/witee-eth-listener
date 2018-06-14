package com.mbr.chain.domain.bo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageCallBack {

	private Long id;
	private String httpUrl;
	private String orderId;
	private Long ethTransactionId;
	private Integer status;// 0 成功，1 失败，3 重复次数超时
	private Date createTime;
	private Date updateTime;
	private Integer failRecord;

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
	 * @return the httpUrl
	 */
	public String getHttpUrl() {
		return httpUrl;
	}

	/**
	 * @param httpUrl
	 *            the httpUrl to set
	 */
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
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
	 * @return the ethTransactionId
	 */
	public Long getEthTransactionId() {
		return ethTransactionId;
	}

	/**
	 * @param ethTransactionId
	 *            the ethTransactionId to set
	 */
	public void setEthTransactionId(Long ethTransactionId) {
		this.ethTransactionId = ethTransactionId;
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

	/**
	 * @return the failRecord
	 */
	public Integer getFailRecord() {
		return failRecord;
	}

	/**
	 * @param failRecord
	 *            the failRecord to set
	 */
	public void setFailRecord(Integer failRecord) {
		this.failRecord = failRecord;
	}

}
