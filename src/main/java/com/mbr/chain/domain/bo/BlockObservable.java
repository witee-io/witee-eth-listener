package com.mbr.chain.domain.bo;

import lombok.Data;

import java.math.BigInteger;

@Data
public class BlockObservable {
	private Long id;
	private BigInteger start;
	private BigInteger end;
	private BigInteger number;

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
	 * @return the start
	 */
	public BigInteger getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(BigInteger start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public BigInteger getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(BigInteger end) {
		this.end = end;
	}

	/**
	 * @return the number
	 */
	public BigInteger getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(BigInteger number) {
		this.number = number;
	}

}
