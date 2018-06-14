package com.mbr.chain.domain.dto.response;

import lombok.Data;

@Data
public class PageResult<T> {

	private Long total;
	private T list;

	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * @return the list
	 */
	public T getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(T list) {
		this.list = list;
	}

}
