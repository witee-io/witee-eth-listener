package com.mbr.chain.domain.dto.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ListenerAddressRequest implements Serializable {

	@NotNull
	private Long channel;

	private String address;
	private String deviceId;
	private boolean single;


	private List<ListenerAddressTokenRequest> listenerAddressToken;

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
	 * @return the single
	 */
	public boolean isSingle() {
		return single;
	}

	/**
	 * @param single
	 *            the single to set
	 */
	public void setSingle(boolean single) {
		this.single = single;
	}

	/**
	 * @return the listenerAddressToken
	 */
	public List<ListenerAddressTokenRequest> getListenerAddressToken() {
		return listenerAddressToken;
	}

	/**
	 * @param listenerAddressToken
	 *            the listenerAddressToken to set
	 */
	public void setListenerAddressToken(List<ListenerAddressTokenRequest> listenerAddressToken) {
		this.listenerAddressToken = listenerAddressToken;
	}

}
