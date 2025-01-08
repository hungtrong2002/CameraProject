package com.camera.modelAPI;

import java.util.List;

public class Result {
	private Long offset;
	private Long limit;
	private Long total;
	private List<Webcams> webcams;
	public Long getOffset() {
		return offset;
	}
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	public Long getLimit() {
		return limit;
	}
	public void setLimit(Long limit) {
		this.limit = limit;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<Webcams> getWebcams() {
		return webcams;
	}
	public void setWebcams(List<Webcams> webcams) {
		this.webcams = webcams;
	}
	
}
