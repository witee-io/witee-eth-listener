package com.mbr.chain.domain.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EthOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3612658470608621165L;

	private Long id;

	private String address;//

	private Date createTime;

	private Date updateTime;

	private String deviceId;

	@NotNull
	private Long channel;

	private List<EthTokenOrder> tokenOrders;

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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the channel
	 */
	public Long getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(Long channel) {
		this.channel = channel;
	}

	/**
	 * @return the tokenOrders
	 */
	public List<EthTokenOrder> getTokenOrders() {
		return tokenOrders;
	}

	/**
	 * @param tokenOrders
	 *            the tokenOrders to set
	 */
	public void setTokenOrders(List<EthTokenOrder> tokenOrders) {
		this.tokenOrders = tokenOrders;
	}

}
