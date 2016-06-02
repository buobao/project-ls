package com.vocinno.utils.network.service;

public class ServiceHandleResultVO implements Cloneable {
	private String name;
	private String type;
	private int status;
	private Object resultData;
	private long timestamp;
	private Object errorMessage;

	public void setErrorMessage(Object msg) {
		this.errorMessage = msg;
	}

	public Object getErrorMessage() {
		return this.errorMessage;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}

	@Override
	public ServiceHandleResultVO clone() {
		try {
			return (ServiceHandleResultVO) super.clone();
		} catch (CloneNotSupportedException e) {
			return new ServiceHandleResultVO();
		}
	}

}
