package com.sandu.cloud.common.vo;

import java.util.List;

public class PageResultDto<T> {

	private List<T> list;
	private long total;
	
	public PageResultDto(){
		
	}
	
	public PageResultDto(List<T> list, long total) {
		super();
		this.list = list;
		this.total = total;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	
}
