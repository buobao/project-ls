package com.vocinno.centanet.model;

public class Params {
	private String type = "";
	private String action = "";
	private String data = "";
	private boolean isAppend = false;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean getIsAppend() {
		if ("append".equals(action)) {
			isAppend = true;
		} else {
			isAppend = false;
		}
		return isAppend;
	}

}
