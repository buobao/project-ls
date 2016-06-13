package com.vocinno.centanet.model;

import java.io.Serializable;

public class PinItem implements Serializable{
	private boolean isSuccess;
	private String msg;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
