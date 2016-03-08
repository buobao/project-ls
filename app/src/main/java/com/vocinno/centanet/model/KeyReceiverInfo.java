package com.vocinno.centanet.model;

public class KeyReceiverInfo {
	private String expireTime = "";
	private String keyNum = "";
	private KeyReceiver receiverInfo = null;
	private String result = "";
	private String type = "";			

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(String keyNum) {
		this.keyNum = keyNum;
	}

	public KeyReceiver getReceiverInfo() {
		return receiverInfo;
	}

	public void setReceiverInfo(KeyReceiver receiverInfo) {
		this.receiverInfo = receiverInfo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
