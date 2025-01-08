package com.camera.common;

import com.camera.modelAPI.Result;

public class APIWindyResponse {
	private String status;
	private Result result;
	
	public APIWindyResponse() {
		this.status = status;
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	
}
